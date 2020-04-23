package org.newtonproject.newtoncore.android.views.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.HomeRouter;
import org.newtonproject.newtoncore.android.router.ValidateBackupRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModel;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModelFactory;
import org.newtonproject.newtoncore.android.widget.CheckDialog;
import org.newtonproject.newtoncore.android.widget.EnterPasswordPopupWindow;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class BackupActivity extends BaseImplActivity<WalletsViewModel> {

    private static final String TAG = "BackupActivity";

    @Inject
    WalletsViewModelFactory factory;

    @BindView(R.id.backup_action)
    Button backupButton;
    @BindView(R.id.rootView)
    View rootView;
    @BindView(R.id.succeedlayout)
    ConstraintLayout succeedlayout;

    @BindView(R.id.mnemonicTextView)
    TextView mnemonicTextView;

    @BindView(R.id.continueButton)
    Button continueButton;

    @BindView(R.id.backuplayout)
    ConstraintLayout backuplayout;

    private ArrayList<String> mnemonic;
    private String walletName;
    private String password;
    private EnterPasswordPopupWindow popuwindow;
    private Wallet currentWallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //initData();
    }

    @Override
    protected int getActivityTitle() {
        return -1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_backup;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, factory).get(WalletsViewModel.class);
        mViewModel.exportedMnemonicStore().observe(this, this::onExportMnemonic);
        mViewModel.defaultWallet().observe(this, this::onDefaultWallet);
        mViewModel.onCommonError().observe(this, this::onCommonError);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.exportWalletError().observe(this, this::onExportWalletError);
        mViewModel.fetchWallets();
    }

    private void onExportWalletError(Throwable throwable) {
        toast(throwable.getMessage());
    }

    private void onDefaultWallet(Wallet wallet) {
        currentWallet = wallet;
    }

    private void onExportMnemonic(ArrayList<String> strings) {
        mnemonic = strings;
        mnemonicTextView.setText(mnemonic.toString().replace("[", "").replace("]", "").replace(",", "  "));
        succeedlayout.setVisibility(View.GONE);
        backuplayout.setVisibility(View.VISIBLE);
    }

//    private void initData() {
//        Intent intent = getIntent();
//        mnemonic = intent.getStringArrayListExtra(C.EXTRA_MNEMONIC);
//        password = intent.getStringExtra(C.EXTRA_PASSWORD);
//        walletName = intent.getStringExtra(C.EXTRA_NAME);
//        mnemonicTextView.setText(mnemonic.toString().replace("[", "").replace("]", "").replace(",", "  "));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.REQUEST_CODE_VALIDATE && resultCode == RESULT_OK) {
            new HomeRouter().open(this);
        }
    }

    @Override
    public void onBackPressed() {
        boolean backupPage = backuplayout.getVisibility() == View.VISIBLE;
        if (backupPage) {
            backuplayout.setVisibility(View.GONE);
            succeedlayout.setVisibility(View.VISIBLE);
            return;
        } else {
            new HomeRouter().open(this);
        }
        super.onBackPressed();
    }

    @OnClick({R.id.backup_action, R.id.continueButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backup_action:
                ArrayList<String> list = new ArrayList<>();
                list.add(getString(R.string.dialog_backup_info1));
                list.add(getString(R.string.dialog_backup_info2));
                list.add(getString(R.string.dialog_backup_info3));
                showSecurityDialog(list, this::showEnterPasswordView);
                break;
            case R.id.continueButton:
                new ValidateBackupRouter().open(this, false, mnemonic, password, walletName);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_skip) {
            new HomeRouter().open(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEnterPasswordView() {
        popuwindow = new EnterPasswordPopupWindow(mContext, password -> {
            mViewModel.exportWalletMnemonic(currentWallet, password);
            popuwindow.dismiss();
            popuwindow = null;
        });

        popuwindow.showAtLocation(rootView, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
    }

    private void showSecurityDialog(ArrayList<String> strings, CheckDialog.OnContinueListener listener) {
        CheckDialog dialog = new CheckDialog();
        dialog.setMargin(16);
        dialog.setOutCancel(true);
        dialog.setOnContinueListener(listener);
        dialog.show(getSupportFragmentManager());
        dialog.setData(strings);
    }
}
