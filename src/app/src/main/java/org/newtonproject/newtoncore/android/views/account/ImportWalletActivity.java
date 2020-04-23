package org.newtonproject.newtoncore.android.views.account;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.router.UserTermsRouter;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.widget.EditTextWatcher;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.Validators;
import org.newtonproject.newtoncore.android.viewmodels.ImportWalletViewModel;
import org.newtonproject.newtoncore.android.viewmodels.ImportWalletViewModelFactory;
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
public class ImportWalletActivity extends BaseImplActivity<ImportWalletViewModel> {


    @Inject
    ImportWalletViewModelFactory importWalletViewModelFactory;

    private static final int MNEMONIC = 1;
    private static final int KEYSTORE = 2;
    private static final int PRIVATEKEY = 3;
    private int CURRENT_PAGE = MNEMONIC;

    @BindView(R.id.importLeft)
    TextView importLeft;
    @BindView(R.id.importMiddle)
    TextView importMiddle;
    @BindView(R.id.importRight)
    TextView importRight;
    @BindView(R.id.importEdittext)
    EditText importEdittext;

    @BindView(R.id.errorTextView)
    TextView errorTextView;
    @BindView(R.id.keystorePasswordLayout)
    HelperEditTextLayout keystorePasswordLayout;
    @BindView(R.id.termOfServiceTextView1)
    TextView termOfServiceTextView1;
    @BindView(R.id.continueButton)
    Button continueButton;
    @BindView(R.id.importLayout)
    ConstraintLayout importLayout;
    @BindView(R.id.walletNameLayout)
    HelperEditTextLayout walletNameLayout;
    @BindView(R.id.passwordLayout)
    HelperEditTextLayout passwordLayout;
    @BindView(R.id.confirmPasswordLayout)
    HelperEditTextLayout confirmPasswordLayout;
    @BindView(R.id.termOfServiceTextView)
    TextView termOfServiceTextView;
    @BindView(R.id.createWalletButton)
    Button createWalletButton;
    @BindView(R.id.settingLayout)
    ConstraintLayout settingLayout;


    @Override
    protected int getActivityTitle() {
        return -1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, importWalletViewModelFactory).get(ImportWalletViewModel.class);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.onCommonError().observe(this, this::onCommonError);
        mViewModel.onCheckKeyStore().observe(this, this::onCheckKeystore);
        mViewModel.onWallet().observe(this, this::onWallet);
        mViewModel.onImportError().observe(this, this::onImportError);
        updateSelectedPage();
    }

    private void onImportError(Throwable throwable) {
        toast(throwable.getMessage());
    }

    @Override
    protected void initView() {
        passwordLayout.addTextChangedListener(new EditTextWatcher(passwordLayout.getEditTextView(), EditTextWatcher.WatcherType.NO_SPACE));
        confirmPasswordLayout.addTextChangedListener(new EditTextWatcher(confirmPasswordLayout.getEditTextView(), EditTextWatcher.WatcherType.NO_SPACE));
    }

    private void onWallet(Wallet wallet) {
        mViewModel.openConfirm(mContext, wallet.address);
        AccountManager.getInstance().initAccount(wallet);
        finish();
    }

    private void onCheckKeystore(Boolean aBoolean) {
        if(aBoolean) {
            setUpWallet();
        }else{
            errorTextView.setText(R.string.keystore_error);
        }
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @OnClick({R.id.importLeft,
            R.id.importMiddle,
            R.id.importRight,
            R.id.termOfServiceTextView1,
            R.id.continueButton,
            R.id.termOfServiceTextView,
            R.id.createWalletButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.importLeft:
                CURRENT_PAGE = MNEMONIC;
                updateSelectedPage();
                break;
            case R.id.importMiddle:
                CURRENT_PAGE = KEYSTORE;
                updateSelectedPage();
                break;
            case R.id.importRight:
                CURRENT_PAGE = PRIVATEKEY;
                updateSelectedPage();
                break;
            case R.id.continueButton:
                preCheckInputContent();
                break;
            case R.id.termOfServiceTextView1:
            case R.id.termOfServiceTextView:
                new UserTermsRouter().open(mContext);
                break;
            case R.id.createWalletButton:
                boolean checkName = walletNameLayout.checkEmpty(R.string.not_null_name);
                boolean checkPassword = passwordLayout.checkPassword(R.string.invalid_password);
                boolean checkConfirm = confirmPasswordLayout.checkEquals(passwordLayout.getEditText(), R.string.password_not_equal);
                boolean flag = !checkName && checkPassword && checkConfirm;
                if(!flag) {
                    return;
                }
                ArrayList<String> list = new ArrayList<>();
                list.add(getString(R.string.create_wallet_tip1));
                list.add(getString(R.string.create_wallet_tip2));
                showSecurityDialog(list, this::startImportWallet);
                break;
        }
    }
    private void startImportWallet() {
        String name = walletNameLayout.getEditText();
        String newPassword = passwordLayout.getEditText();
        String oldPassword = keystorePasswordLayout.getEditText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.hideKeyboard(confirmPasswordLayout.getEditTextView());
                KeyboardUtils.hideKeyboard(passwordLayout.getEditTextView());
                KeyboardUtils.hideKeyboard(keystorePasswordLayout.getEditTextView());
                KeyboardUtils.hideKeyboard(importEdittext);
            }
        },100);
        String content = importEdittext.getText().toString().trim();
        switch (CURRENT_PAGE) {
            case MNEMONIC:
                mViewModel.onMnemonic(StringUtil.getMnemonicFromString(content), name, newPassword);
                break;
            case KEYSTORE:
                mViewModel.importKeysotreWithNewPassword(name, content, oldPassword, newPassword);
                break;
            case PRIVATEKEY:
                mViewModel.onPrivateKey(content, name, newPassword);
                break;
        }
    }

    private void setUpWallet() {
        importLayout.setVisibility(View.GONE);
        settingLayout.setVisibility(View.VISIBLE);
    }

    private void preCheckInputContent() {
        String content = importEdittext.getText().toString().trim();
        if(TextUtils.isEmpty(content)) {
            errorTextView.setText(R.string.not_null_import_content);
            return;
        }
        switch (CURRENT_PAGE) {
            case MNEMONIC:
                if(Validators.checkMnemonic(content)) {
                    setUpWallet();
                }else{
                    errorTextView.setText(R.string.mnemonic_error);
                }
                break;
            case KEYSTORE:
                String password = keystorePasswordLayout.getEditText();
                mViewModel.checkKeystore(content, password);
                break;
            case PRIVATEKEY:
                if(Validators.validatePrivateKey(content)){
                    setUpWallet();
                }else{
                    errorTextView.setText(R.string.invalid_privatekey);
                }
                break;
        }
    }

    private void updateSelectedPage() {
        updateTextView();
        updatePage();
    }

    private void updatePage() {
        keystorePasswordLayout.setVisibility(CURRENT_PAGE == KEYSTORE ? View.VISIBLE : View.GONE);
        importEdittext.setHint(getEditHint());
    }

    private void updateTextView() {
        importLeft.setSelected(CURRENT_PAGE == MNEMONIC);
        importLeft.setTextColor(CURRENT_PAGE == MNEMONIC ? getResources().getColor(R.color.bgColor) : getResources().getColor(R.color.mainColor));
        importMiddle.setSelected(CURRENT_PAGE == KEYSTORE);
        importMiddle.setTextColor(CURRENT_PAGE == KEYSTORE ? getResources().getColor(R.color.bgColor) : getResources().getColor(R.color.mainColor));
        importRight.setSelected(CURRENT_PAGE == PRIVATEKEY);
        importRight.setTextColor(CURRENT_PAGE == PRIVATEKEY ? getResources().getColor(R.color.bgColor) : getResources().getColor(R.color.mainColor));
    }

    private @StringRes int getEditHint() {
        int res = 0;
        switch (CURRENT_PAGE) {
            case MNEMONIC:
                res = R.string.hint_mnemonic;
                break;
            case KEYSTORE:
                res = R.string.hint_keystore;
                break;
            case PRIVATEKEY:
                res = R.string.hint_privateKey;
                break;
        }
        return res;
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
