package org.newtonproject.newtoncore.android.views.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.data.service.FingerprintUiHelper;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.utils.KeyboardUtils;
import org.newtonproject.newtoncore.android.widget.FingerprintAuthenticationDialogFragment;
import org.newtonproject.newtoncore.android.widget.PasswordEdittext;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/11/10--10:40 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AuthPinActivity extends BaseActivity implements PasswordEdittext.OnPasswordListener {

    @BindView(R.id.pinEdittext)
    PasswordEdittext pinEdittext;
    @BindView(R.id.tipTextView)
    TextView tipTextView;
    @BindView(R.id.centerTitle)
    TextView centerTitle;

    PreferenceRepositoryType preference;
    private Unbinder unbinder;
    private String DIALOG_FRAGMENT = "AuthDialog";
    public static final int ADD_AUTH_TYPE = 0;
    public static final int VERIFY_AUTH_TYPE = 1;
    public static final int CLOSE_AUTH_TYPE = 2;
    private int authType = ADD_AUTH_TYPE;

    private int inputIndex = 0;
    private String firstPassword;
    private FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment;
    private boolean isSupportFinger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_pin);
        authType = getIntent().getIntExtra(C.EXTRA_AUTH_TYPE, ADD_AUTH_TYPE);
        preference = new SharedPreferenceRepository(this);
        unbinder = ButterKnife.bind(this);
        pinEdittext.setOnPasswordListener(this);
        isSupportFinger = preference.isSupportFinger();
        toolbar();
        initViewByType();
        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(e-> KeyboardUtils.showKeyboard(pinEdittext));
    }

    private void initViewByType() {
        switch (authType) {
            case ADD_AUTH_TYPE:
                centerTitle.setText(R.string.set_password);
                tipTextView.setText(R.string.pin_code);
                break;
            case VERIFY_AUTH_TYPE:
                centerTitle.setText(R.string.verify_password);
                tipTextView.setText(R.string.verify_password);
                if(!isSupportFinger) {
                    break;
                }
                fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
                fingerprintAuthenticationDialogFragment.setFingerprintUiHelperCallback(new FingerprintUiHelper.Callback() {
                    @Override
                    public void onAuthenticated() {
                        fingerprintAuthenticationDialogFragment.dismiss();
                        preference.setIsCheckPin(false);
                        finish();
                    }

                    @Override
                    public void onError() {
                        fingerprintAuthenticationDialogFragment.dismiss();
                        Toast.makeText(AuthPinActivity.this, R.string.finger_error, Toast.LENGTH_SHORT).show();
                    }
                });
                fingerprintAuthenticationDialogFragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT);
                break;
            case CLOSE_AUTH_TYPE:
                centerTitle.setText(R.string.verify_password);
                tipTextView.setText(R.string.verify_password);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onComplete(String password) {
        String pinPassword = preference.getPinPassword();
        switch (authType) {
            case ADD_AUTH_TYPE:
                if(inputIndex == 0) {
                    firstPassword = password;
                    pinEdittext.clean();
                    inputIndex = inputIndex + 1;
                    tipTextView.setText(R.string.pin_code_again);
                }else{
                    if(firstPassword.equals(password)) {
                        preference.setIsCheckPin(true);
                        preference.setIsPin(true);
                        preference.setPinPassword(firstPassword);
                        finish();
                    }else{
                        Toast.makeText(this, R.string.password_not_equal, Toast.LENGTH_SHORT).show();
                        tipTextView.setText(R.string.pin_code);
                        pinEdittext.clean();
                        inputIndex = 0;
                    }
                }
                break;
            case VERIFY_AUTH_TYPE:
                if(password.equals(pinPassword)) {
                    preference.setIsCheckPin(false);
                    finish();
                }else{
                    Toast.makeText(this, R.string.password_error, Toast.LENGTH_SHORT).show();
                    pinEdittext.clean();
                }
                break;
            case CLOSE_AUTH_TYPE:
                if(password.equals(pinPassword)) {
                    preference.setIsPin(false);
                    preference.setPinPassword(null);
                    preference.setIsCheckPin(false);
                    preference.setIsSupportFinger(false);
                    finish();
                }else{
                    Toast.makeText(this, R.string.password_error, Toast.LENGTH_SHORT).show();
                    pinEdittext.clean();
                }
                break;
        }
    }
}
