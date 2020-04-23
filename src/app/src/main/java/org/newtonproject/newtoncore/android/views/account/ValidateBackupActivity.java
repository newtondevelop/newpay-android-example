package org.newtonproject.newtoncore.android.views.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.ErrorEnvelope;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.viewmodels.ImportWalletViewModel;
import org.newtonproject.newtoncore.android.viewmodels.ImportWalletViewModelFactory;

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
public class ValidateBackupActivity extends BaseImplActivity<ImportWalletViewModel> implements View.OnClickListener {

    @Inject
    ImportWalletViewModelFactory importWalletViewModelFactory;

    @BindView(R.id.mnemonicEditText)
    EditText mnemonicEditText;


    private ArrayList<String> mnemonic;

    private String walletName;

    private String password;

    @Override
    protected void progress(boolean flag) {
        onProgress(flag, getString(R.string.importing));
    }

    @Override
    protected int getActivityTitle() {
        return -1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_validate_backup;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mnemonic = intent.getStringArrayListExtra(C.EXTRA_MNEMONIC);
        password = intent.getStringExtra(C.EXTRA_PASSWORD);
        walletName = intent.getStringExtra(C.EXTRA_NAME);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, importWalletViewModelFactory)
                .get(ImportWalletViewModel.class);
        mViewModel.error().observe(this, this::onError);
        mViewModel.onWallet().observe(this, this::onWallet);
        mViewModel.progress().observe(this, this::progress);
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        toast(errorEnvelope.message);
    }

    private void onWallet(Wallet wallet) {
//        mViewModel.sendAddress(wallet.address);
        mViewModel.openConfirm(mContext, wallet.address);
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.continueButton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueButton:
                checkAndImportWallet();
                break;
            default:
                break;
        }
    }

    private void checkAndImportWallet() {
        String str = mnemonicEditText.getText().toString().trim();
        if(TextUtils.isEmpty(str)) {
            toast(R.string.invalid_mnemonic_cannot_null);
            return;
        }
        ArrayList<String> list = StringUtil.getMnemonicFromString(str);
        String s = list.toString();
        String s1 = mnemonic.toString();
        if(!TextUtils.equals(s, s1)) {
            toast(R.string.invalid_mnemonic);
            return;
        }
        mViewModel.closeBackUp();
        mViewModel.openConfirm(mContext);
        setResult(RESULT_OK);
        finish();
        //mViewModel.onMnemonic(mnemonic, walletName, password);
    }



}
