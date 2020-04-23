package org.newtonproject.newtoncore.android.views.contact;

import android.arch.lifecycle.ViewModelProviders;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.utils.Validators;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.viewmodels.UpdatePasswordModelFactory;
import org.newtonproject.newtoncore.android.viewmodels.UpdatePasswordViewModel;

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
public class UpdateWalletPasswordActivity extends BaseImplActivity<UpdatePasswordViewModel> {

    @Inject
    UpdatePasswordModelFactory updatePasswordModelFactory;

    @BindView(R.id.oldPasswordEdittext)
    EditText oldPasswordEdittext;
    @BindView(R.id.newPasswordEdittext)
    EditText newPasswordEdittext;
    @BindView(R.id.confirmNewPasswordEdittext)
    EditText confirmNewPasswordEdittext;
    @BindView(R.id.confirm_button)
    Button confirmButton;

    private String walletAddress;

    @Override
    protected int getActivityTitle() {
        return R.string.update_password;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void initViewModel() {
        String address = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        if(address == null) {
            finish();
        }
        this.walletAddress = address;
        mViewModel = ViewModelProviders.of(this, updatePasswordModelFactory).get(UpdatePasswordViewModel.class);
        mViewModel.onWallets().observe(this, this::onWallet);
        mViewModel.onUpdateError().observe(this, this::onUpdateError);
        mViewModel.progress().observe(this, this::progress);
    }

    @Override
    protected void progress(boolean show) {
        onProgress(show, getString(R.string.updating_password));
    }

    private void onUpdateError(Throwable errorEnvelope) {
        ViewUtils.buttonToggle(confirmButton, true, R.string.confirm);
        Toast.makeText(this, errorEnvelope.getMessage(), Toast.LENGTH_SHORT).show();
        oldPasswordEdittext.setText("");
        newPasswordEdittext.setText("");
        confirmNewPasswordEdittext.setText("");
    }

    private void onWallet(Wallet[] wallets) {
        ViewUtils.buttonToggle(confirmButton, true, R.string.confirm);
        Toast.makeText(this, R.string.update_password_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick(R.id.confirm_button)
    public void onViewClicked() {
        String oldPassword = oldPasswordEdittext.getText().toString().trim();
        String newpassword = newPasswordEdittext.getText().toString().trim();
        String confirmPassword = confirmNewPasswordEdittext.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(this, String.format("%s %s",getString(R.string.old_password), getString(R.string.can_not_be_null)), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newpassword)) {
            Toast.makeText(this, String.format("%s %s", getString(R.string.label_new_password),getString(R.string.can_not_be_null)), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals(confirmPassword, newpassword)) {
            Toast.makeText(this, R.string.password_not_equal, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Validators.checkPassword(newpassword)) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            return;
        }
        ViewUtils.buttonToggle(confirmButton, false, R.string.updating_password);
        mViewModel.updateWallet(new Wallet(walletAddress), oldPassword, confirmPassword);
    }
}
