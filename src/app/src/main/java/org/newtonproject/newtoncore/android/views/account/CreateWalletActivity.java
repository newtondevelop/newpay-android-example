package org.newtonproject.newtoncore.android.views.account;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ErrorEnvelope;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.router.BackupRouter;
import org.newtonproject.newtoncore.android.router.UserTermsRouter;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.widget.EditTextWatcher;
import org.newtonproject.newtoncore.android.utils.Validators;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModel;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModelFactory;
import org.newtonproject.newtoncore.android.widget.AllCapTransformationMethod;
import org.newtonproject.newtoncore.android.widget.CheckDialog;
import org.newtonproject.newtoncore.android.widget.HelperEditTextLayout;

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
public class CreateWalletActivity extends BaseImplActivity<WalletsViewModel> {

    @Inject
    WalletsViewModelFactory walletsViewModelFactory;
    @BindView(R.id.walletNameLayout)
    HelperEditTextLayout walletNameLayout;
    @BindView(R.id.passwordLayout)
    HelperEditTextLayout passwordLayout;
    @BindView(R.id.confirmPasswordLayout)
    HelperEditTextLayout confirmPasswordLayout;
    @BindView(R.id.inviteCodeLayout)
    HelperEditTextLayout inviteCodeLayout;
    @BindView(R.id.create_account_button)
    Button createButton;

    private String walletPassword;
    private String name;
    private Wallet createdWallet;
    private ArrayList<String> mnemonicList;


    @Override
    protected int getActivityTitle() {
        return -1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_wallet;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, walletsViewModelFactory).get(WalletsViewModel.class);
        mViewModel.error().observe(this, this::onError);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.createdWallet().observe(this, this::onCreateWallet);
        mViewModel.createWalletMnemonic().observe(this, this::onCreateWalletMnemonic);
        mViewModel.wallets().observe(this, this::onDefaultWallet);
        mViewModel.exportedMnemonicStore().observe(this, this::onExportMnemonic);
        mViewModel.onCreateWallet().observe(this, this::onCreateWalletDirect);
    }

    @Override
    protected void initView() {
        passwordLayout.addTextChangedListener(new EditTextWatcher(passwordLayout.getEditTextView(), EditTextWatcher.WatcherType.NO_SPACE));
        confirmPasswordLayout.addTextChangedListener(new EditTextWatcher(confirmPasswordLayout.getEditTextView(), EditTextWatcher.WatcherType.NO_SPACE));
        inviteCodeLayout.getEditTextView().setTransformationMethod(new AllCapTransformationMethod(true));
    }

    private void onCreateWalletDirect(Wallet wallet) {
//        mViewModel.sendAddress(wallet.address);
        mViewModel.openConfirm(mContext, wallet.address);
        AccountManager.getInstance().initAccount(wallet);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewUtils.buttonToggle(createButton, true, R.string.action_create_new_account);
    }

    private void onCreateWalletMnemonic(ArrayList<String> strings) {
        if (null != strings) {
            ViewUtils.buttonToggle(createButton, true, R.string.action_create_new_account);
            new BackupRouter().open(this, false, strings, walletPassword, name);
        }
    }

    private void onDefaultWallet(Wallet[] wallets) {
        if (null != mnemonicList) {
            new BackupRouter().open(this, false, mnemonicList, walletPassword, name);
        }
    }


    private void onCreateWallet(Wallet wallet) {
        this.createdWallet = wallet;
        mViewModel.exportWalletMnemonic(wallet, walletPassword);
    }

    private void onExportMnemonic(ArrayList<String> strings) {
        this.mnemonicList = strings;
        mViewModel.deleteWallet(createdWallet, walletPassword);
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        ViewUtils.buttonToggle(createButton, true, R.string.action_create_new_account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.REQUEST_CODE_BACKUP && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnClick({R.id.create_account_button, R.id.termOfServiceTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_account_button:
                createWallet();
                break;
            case R.id.termOfServiceTextView:
                new UserTermsRouter().open(mContext);
                break;
        }
    }

    private void createWallet() {
        String name = walletNameLayout.getEditText();
        String password = passwordLayout.getEditText();
        String rePassword = confirmPasswordLayout.getEditText();
        String inviteCode = inviteCodeLayout.getEditText();
        mViewModel.setInviteCode(inviteCode);
        if (TextUtils.isEmpty(name)) {
            walletNameLayout.setError(R.string.not_null_name);
            return;
        }else{
            walletNameLayout.hideError();
        }
        if (!Validators.checkPassword(password)) {
            passwordLayout.setError(R.string.hint_password);
            return;
        }else {
            passwordLayout.hideError();
        }
        if (TextUtils.isEmpty(rePassword)) {
            confirmPasswordLayout.setError(R.string.hint_password);
            return;
        }else{
            confirmPasswordLayout.hideError();
        }
        if (!TextUtils.equals(password, rePassword)) {
            confirmPasswordLayout.setError(R.string.password_not_equal);
            return;
        }else{
            confirmPasswordLayout.hideError();
        }
        this.name = name;
        this.walletPassword = password;
        if(C.CREATE_WALLET_DIRECT) {
            ArrayList<String> list = new ArrayList<>();
            list.add(getString(R.string.create_wallet_tip1));
            list.add(getString(R.string.create_wallet_tip2));
            showSecurityDialog(list, ()-> mViewModel.createWalletDirect(walletPassword, name));
        }
    }

    private void showSecurityDialog(ArrayList<String> data, CheckDialog.OnContinueListener listener) {
        CheckDialog dialog = new CheckDialog();
        dialog.setMargin(16);
        dialog.setOutCancel(true);
        dialog.setData(data);
        dialog.setOnContinueListener(listener);
        dialog.show(getSupportFragmentManager());
    }

}
