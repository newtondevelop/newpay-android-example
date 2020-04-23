package org.newtonproject.newtoncore.android.views.account;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ErrorEnvelope;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.CreateWalletRouter;
import org.newtonproject.newtoncore.android.router.HomeRouter;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.views.widget.adapter.WalletsAdapter;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModel;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModelFactory;
import org.newtonproject.newtoncore.android.widget.AddWalletView;
import org.newtonproject.newtoncore.android.widget.BackupView;
import org.newtonproject.newtoncore.android.widget.BackupWarningView;
import org.newtonproject.newtoncore.android.widget.SystemView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static org.newtonproject.newtoncore.android.C.REQUEST_CODE_IMPORT_WALLET;
import static org.newtonproject.newtoncore.android.C.REQUEST_CODE_SHARE;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletsActivity extends BaseActivity implements
		View.OnClickListener,
        AddWalletView.OnNewWalletClickListener,
        AddWalletView.OnImportWalletClickListener {

	@Inject
    WalletsViewModelFactory walletsViewModelFactory;
	WalletsViewModel viewModel;

	private WalletsAdapter adapter;

	private SystemView systemView;
    private BackupWarningView backupWarning;
    private Dialog dialog;
    private boolean isSetDefault;
    private final Handler handler = new Handler();

    @Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wallets);
		// Init toolbar

		adapter = new WalletsAdapter(this::onSetWalletDefault, this::onDeleteWallet, this::onExportWallet);
		SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
		systemView = findViewById(R.id.system_view);
		backupWarning = findViewById(R.id.backup_warning);

		RecyclerView list = findViewById(R.id.list);

		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(adapter);

		systemView.attachRecyclerView(list);
		systemView.attachSwipeRefreshLayout(refreshLayout);
		backupWarning.setOnPositiveClickListener(this::onNowBackup);

		viewModel = ViewModelProviders.of(this, walletsViewModelFactory)
				.get(WalletsViewModel.class);

		viewModel.error().observe(this, this::onError);
		viewModel.progress().observe(this, systemView::showProgress);
		viewModel.wallets().observe(this, this::onFetchWallet);
		viewModel.defaultWallet().observe(this, this::onChangeDefaultWallet);
		viewModel.createdWallet().observe(this, this::onCreatedWallet);
		viewModel.exportedStore().observe(this, this::openShareDialog);

		refreshLayout.setOnRefreshListener(viewModel::fetchWallets);
	}

    private void onExportWallet(Wallet wallet) {
        showBackupDialog(wallet, false);
    }

    @Override
	protected void onPause() {
		super.onPause();

		hideDialog();
	}

	@Override
	public void onBackPressed() {
		// User can't start work without wallet.
		if (adapter.getItemCount() > 0) {
			viewModel.showTransactions(this);
		} else {
			finish();
			System.exit(0);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    if (adapter.getItemCount() > 0) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
            case R.id.action_add: {
                onAddWallet();
            } break;
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == C.REQUEST_CODE_CREATE_WALLET && resultCode == RESULT_OK) {
			finish();
			return;
		}
		if (requestCode == REQUEST_CODE_IMPORT_WALLET) {
			showToolbar();
		    if (resultCode == RESULT_OK) {
				new HomeRouter().open(this);
				finish();
            }
            return;
        }
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.try_again: {
				viewModel.fetchWallets();
			} break;
		}
	}

	@Override
	public void onNewWallet(View view) {
		new CreateWalletRouter().open(this, false);
	}

	@Override
	public void onImportWallet(View view) {
		hideDialog();
		viewModel.importWallet(this);
	}

	private void onAddWallet() {
		AddWalletView addWalletView = new AddWalletView(this);
		addWalletView.setOnNewWalletClickListener(this);
		addWalletView.setOnImportWalletClickListener(this);
		dialog = new BottomSheetDialog(this);
		dialog.setContentView(addWalletView);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void onChangeDefaultWallet(Wallet wallet) {
        if (isSetDefault) {
            viewModel.showTransactions(this);
        } else {
            adapter.setDefaultWallet(wallet);
        }
	}

	private void onFetchWallet(Wallet[] wallets) {
		if (wallets == null || wallets.length == 0) {
			AddWalletView addWalletView = new AddWalletView(this, R.layout.layout_empty_add_account);
			addWalletView.setOnNewWalletClickListener(this);
			addWalletView.setOnImportWalletClickListener(this);
			systemView.showEmpty(addWalletView);
			adapter.setWallets(new Wallet[0]);
            hideToolbar();
        } else {
			enableDisplayHomeAsUp();
			adapter.setWallets(wallets);
        }
		invalidateOptionsMenu();
	}

	private void onCreatedWallet(Wallet wallet) {
//        hideToolbar();
//        backupWarning.show(wallet);
	}

	private void onLaterBackup(View view, Wallet wallet) {
        showNoBackupWarning(wallet);
    }

    private void onNowBackup(View view, Wallet wallet) {
        showBackupDialog(wallet, true);
    }

    private void showNoBackupWarning(Wallet wallet) {
        dialog = buildDialog()
                .setTitle(getString(R.string.title_dialog_watch_out))
                .setMessage(getString(R.string.dialog_message_unrecoverable_message))
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(R.string.i_understand, (dialog, whichButton) -> {
                    backupWarning.hide();
                    showToolbar();
                })
                .setNegativeButton(android.R.string.cancel, null) //(dialog, whichButton) -> showBackupDialog(wallet, true)
                .create();
        dialog.show();
    }

    private void showBackupDialog(Wallet wallet, boolean isNew) {
	    BackupView view = new BackupView(this);
        dialog = buildDialog()
                .setView(view)
                .setPositiveButton(R.string.ok,
                        (dialogInterface, i) -> {
                            viewModel.exportWallet(wallet, view.getPassword());
                            KeyboardUtils.hideKeyboard(view.findViewById(R.id.password));
                        })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    if (isNew) {
                        onCreatedWallet(wallet);
                    }
                    KeyboardUtils.hideKeyboard(view.findViewById(R.id.password));
                })
                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.password)))
                .create();
        dialog.show();
        handler.postDelayed(() -> {
            KeyboardUtils.showKeyboard(view.findViewById(R.id.password));
        }, 500);

    }

    private void openShareDialog(String jsonData) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, jsonData);
        startActivityForResult(
                Intent.createChooser(sharingIntent, getString(R.string.share_via)),
                REQUEST_CODE_SHARE);
    }

	private void onError(ErrorEnvelope errorEnvelope) {
		systemView.showError(errorEnvelope.message, this);
	}

	private void onSetWalletDefault(Wallet wallet) {
		viewModel.setDefaultWallet(wallet);
		isSetDefault = true;
	}

	private void onDeleteWallet(Wallet wallet, String password) {
		dialog = buildDialog()
				.setTitle(getString(R.string.title_delete_account))
				.setMessage(getString(R.string.confirm_delete_account))
				.setIcon(R.drawable.ic_warning_black_24dp)
				.setPositiveButton(android.R.string.yes, (dialog, btn) -> viewModel.deleteWallet(wallet, password))
				.setNegativeButton(android.R.string.no, null)
				.create();
		dialog.show();
	}

	private AlertDialog.Builder buildDialog() {
	    hideDialog();
	    return new AlertDialog.Builder(this);
    }

	private void hideDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
