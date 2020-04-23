package org.newtonproject.newtoncore.android.views.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.utils.crypto.NewtonWalletFile;
import org.newtonproject.newtoncore.android.data.entity.common.EventMessage;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.ManageWalletsRouter;
import org.newtonproject.newtoncore.android.router.UpdateWalletPasswordRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.contact.UpdateWalletNameActivity;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModel;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModelFactory;
import org.newtonproject.newtoncore.android.widget.CheckDialog;
import org.newtonproject.newtoncore.android.widget.EnterPasswordPopupWindow;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static org.newtonproject.newtoncore.android.C.REQUEST_CODE_SHARE;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletDetailActivity extends BaseImplActivity<WalletsViewModel> {

    private static final String TAG = "WalletDetailActivity";
    @Inject
    WalletsViewModelFactory walletsViewModelFactory;

    @BindView(R.id.centerTitle)
    TextView centerTitle;
    @BindView(R.id.updateNameTextView)
    LinearLayout update_wallet_name;
    @BindView(R.id.updatePasswordTextView)
    LinearLayout updatePasswordTextView;
    @BindView(R.id.exportPrivateKeyTextView)
    LinearLayout exportPrivateKey;
    @BindView(R.id.exportMnemonicTextView)
    LinearLayout exportMnemonic;
    @BindView(R.id.exportKeyStoreTextView)
    LinearLayout exportKeyStore;
    @BindView(R.id.deleteWalletTextView)
    LinearLayout deleteWallet;
    @BindView(R.id.rootView)
    FrameLayout rootView;

    private Wallet wallet;

    private static final int EXPORT_KEYSTORE = 1;
    private static final int EXPORT_PRIVATEKEY = 2;
    private static final int EXPORT_MNEMONIC = 3;
    private static final int DELETE_WALLET = 4;
    private static final int UPDATE_WALLET = 5;
    private EnterPasswordPopupWindow popuwindow;

    @Override
    protected int getActivityTitle() {
        return R.string.operation_wallet;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_operation;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initViewModel() {
        Log.e(TAG, "InitViewModel..");
        mViewModel = ViewModelProviders.of(this, walletsViewModelFactory)
                .get(WalletsViewModel.class);
        mViewModel.exportedMnemonicStore().observe(this, this::openShareDialog);
        mViewModel.exportedStore().observe(this, this::openShareDialog);
        mViewModel.exportPrivateKeyStore().observe(this, this::openShareDialog);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.onCommonError().observe(this, this::onCommonError);
        mViewModel.exportWalletError().observe(this, this::onExportError);
        mViewModel.wallets().observe(this, this::onWallets);
        mViewModel.onDeleteError().observe(this, this::onDeleteError);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletEvent(EventMessage<Wallet> eventMessage) {
        if (eventMessage.type == EventMessage.WALLET_EVENT) {
            wallet = eventMessage.message;
            checkWalletType(wallet.type);
        }
    }

    private void checkWalletType(String walletType) {
        if (TextUtils.isEmpty(walletType)) {
            walletType = NewtonWalletFile.FILETYPE_PRIVATE;
        }
        if (walletType.equals(NewtonWalletFile.FILETYPE_MNEMONIC)) {
            exportMnemonic.setVisibility(View.VISIBLE);
        } else if (walletType.equals(NewtonWalletFile.FILETYPE_PRIVATE)) {
            exportMnemonic.setVisibility(View.GONE);
            exportPrivateKey.setVisibility(View.VISIBLE);
            exportKeyStore.setVisibility(View.VISIBLE);
        }
        Log.e(TAG,"Checked wallet type");
    }

    private void onDeleteError(Throwable errorEnvelope) {
        Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
    }

    private void onExportError(Throwable errorEnvelope) {
        Toast.makeText(this, getString(R.string.export_error), Toast.LENGTH_SHORT).show();
    }

    private void onWallets(Wallet[] wallets) {
        if (null == wallets || wallets.length == 0) {
            mViewModel.setDefaultWallet(null);
            new ManageWalletsRouter().open(this, true);
            //finish();
        }
    }

    private void openShareDialog(String s) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
        startActivityForResult(
                Intent.createChooser(sharingIntent, getString(R.string.share_via)),
                REQUEST_CODE_SHARE);
    }

    private void openShareDialog(ArrayList<String> strings) {
        String mnemonic = strings.toString().replace("[", "").replace("]", "").replace(",", "");
        openShareDialog(mnemonic);
    }

    private void showEnterPasswordPop(Wallet currentWallet, int operationId) {
        popuwindow = new EnterPasswordPopupWindow(this, password -> {

            switch (operationId) {
                case EXPORT_KEYSTORE:
                    mViewModel.exportWallet(currentWallet, password);
                    break;
                case EXPORT_PRIVATEKEY:
                    mViewModel.exportWalletPrivateKey(currentWallet, password);
                    break;
                case EXPORT_MNEMONIC:
                    mViewModel.exportWalletMnemonic(currentWallet, password);
                    break;
                case DELETE_WALLET:
                    mViewModel.deleteWallet(currentWallet, password);
                default:
                    break;
            }
            popuwindow.dismiss();
            popuwindow = null;
        });

        popuwindow.showAtLocation(rootView, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.updateNameTextView, R.id.updatePasswordTextView, R.id.exportPrivateKeyTextView, R.id.exportMnemonicTextView, R.id.exportKeyStoreTextView, R.id.deleteWalletTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exportPrivateKeyTextView:
                showSecurityDialog(getString(R.string.privateKey_title), () -> showEnterPasswordPop(wallet, EXPORT_PRIVATEKEY));
                break;
            case R.id.exportMnemonicTextView:
                showSecurityDialog(getString(R.string.mnemonic_title), () -> showEnterPasswordPop(wallet, EXPORT_MNEMONIC));
                break;
            case R.id.exportKeyStoreTextView:
                showSecurityDialog(getString(R.string.keystore_title), () -> showEnterPasswordPop(wallet, EXPORT_KEYSTORE));
                break;
            case R.id.deleteWalletTextView:
                showWarnDialog(() -> showEnterPasswordPop(wallet, DELETE_WALLET));
                break;
            case R.id.updatePasswordTextView:
                new UpdateWalletPasswordRouter().open(this, wallet.address);
                break;
            case R.id.updateNameTextView:
                Intent intent = new Intent(this, UpdateWalletNameActivity.class);
                intent.putExtra(C.EXTRA_ADDRESS, wallet.address);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void showSecurityDialog(String message, CheckDialog.OnContinueListener listener) {
        CheckDialog dialog = new CheckDialog();
        dialog.setMargin(16);
        dialog.setOutCancel(true);
        dialog.show(getSupportFragmentManager());
        ArrayList<String> list = new ArrayList<>();
        list.add(String.format(getString(R.string.dialog_export_info1), message));
        list.add(String.format(getString(R.string.dialog_export_info2), message));
        dialog.setData(list);
        dialog.setOnContinueListener(listener);
    }

    private void showWarnDialog(CheckDialog.OnContinueListener listener) {
        CheckDialog dialog = new CheckDialog();
        dialog.setMargin(16);
        dialog.setNeedChecked(false);
        dialog.setContinueBg(R.drawable.main_warn_button_bg);
        dialog.setTitle(R.string.delete_wallet);
        dialog.setOutCancel(true);
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.dialog_delete_tip));
        dialog.setData(list);
        dialog.setOnContinueListener(listener);
        dialog.show(getSupportFragmentManager());
    }

}
