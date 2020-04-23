package org.newtonproject.newtoncore.android.views.settings;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.router.Router;
import org.newtonproject.newtoncore.android.data.service.FingerprintUiHelper;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.views.device.AuthPinActivity;
import org.newtonproject.newtoncore.android.utils.LanguageUtils;
import org.newtonproject.newtoncore.android.widget.FingerprintAuthenticationDialogFragment;
import org.newtonproject.newtoncore.android.widget.NoDragSwitchCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NewPaySettingsActivity extends BaseActivity {

    @BindView(R.id.networkLayout)
    LinearLayout networkLayout;
    @BindView(R.id.currentNetNameTextView)
    TextView currentNetNameTextView;
    @BindView(R.id.centerTitle)
    TextView centerTitle;
    @BindView(R.id.languageLayout)
    LinearLayout languageLayout;
    @BindView(R.id.aboutLayout)
    LinearLayout aboutLayout;
    @BindView(R.id.currentLanguageTextView)
    TextView currentLanguageTextView;

    @BindView(R.id.switchCompat)
    NoDragSwitchCompat switchCompat;

    @BindView(R.id.touchIdLayout)
    LinearLayout touchIdLayout;
    @BindView(R.id.touchIdSwitchCompat)
    NoDragSwitchCompat touchIdSwitchCompat;

    private boolean isPin;
    private Context context;
    SharedPreferenceRepository preferenceRepository;
    private Unbinder unbinder;
    private String netType;
    Router router;
    private FingerprintManagerCompat mFingerprintManagerCompat;
    private boolean isSupportFinger = false;
    private FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment;
    private String DIALOG_FRAGMENT_TAG = "DialogFragment";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpay_settings);
        unbinder = ButterKnife.bind(this);
        preferenceRepository = new SharedPreferenceRepository(this);
        centerTitle.setText(R.string.me_settings);
        router = new Router();
        mFingerprintManagerCompat = FingerprintManagerCompat.from(this);
        toolbar();
        checkSupportFinger();
        netType = C.CURRENT_NET == C.NET_TYPE.MAINNET ? "MainNet" : "TestNet";
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentNetNameTextView.setText(String.format("%s %s", preferenceRepository.getDefaultNetwork(), netType));
        currentLanguageTextView.setText(LanguageUtils.getCurrentLanguage(this));
        isPin = preferenceRepository.isPin();
        isSupportFinger = preferenceRepository.isSupportFinger();
        touchIdSwitchCompat.setChecked(isSupportFinger);
        if(switchCompat.isChecked() != isPin) {
            switchCompat.setChecked(isPin);
        }
    }

    private void checkSupportFinger() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hardwareDetected = mFingerprintManagerCompat.isHardwareDetected();
            boolean hasEnrolledFingerprints = mFingerprintManagerCompat.hasEnrolledFingerprints();
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            boolean keyguardSecure = keyguardManager.isKeyguardSecure();
            isSupportFinger = hardwareDetected && hasEnrolledFingerprints && keyguardSecure;
        }
        if(isSupportFinger) {
            touchIdLayout.setVisibility(View.VISIBLE);
            fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
        }else {
            touchIdLayout.setVisibility(View.GONE);
            preferenceRepository.setIsSupportFinger(isSupportFinger);
        }
    }

    @OnClick({R.id.languageLayout,
            R.id.aboutLayout,
            R.id.networkLayout,
            R.id.switchLockAppLayout,
            R.id.touchIdLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.languageLayout:
                router.open(this, LanguageSettingsActivity.class);
                break;
            case R.id.aboutLayout:
                router.open(this, AboutActivity.class);
                break;
            case R.id.networkLayout:
                router.open(this, SwitchNetworkActivity.class);
                break;
            case R.id.switchLockAppLayout:
                checkSwitchStatusAndOpenAuth(!switchCompat.isChecked());
                break;
            case R.id.touchIdLayout:
                if(isSupportPin()) {
                    toggleSupportFinger(!touchIdSwitchCompat.isChecked());}
                break;
        }
    }

    private boolean isSupportPin() {
        boolean flag = switchCompat.isChecked();
        if(!flag) {
            Toast.makeText(context, R.string.need_open_pin, Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    private void toggleSupportFinger(boolean isSupportFinger) {
        if(!isSupportFinger) {
            preferenceRepository.setIsSupportFinger(isSupportFinger);
            touchIdSwitchCompat.setChecked(isSupportFinger);
            return;
        }
        fingerprintAuthenticationDialogFragment.setFingerprintUiHelperCallback(new FingerprintUiHelper.Callback() {
            @Override
            public void onAuthenticated() {
                fingerprintAuthenticationDialogFragment.dismiss();
                preferenceRepository.setIsSupportFinger(isSupportFinger);
                touchIdSwitchCompat.setChecked(isSupportFinger);
            }

            @Override
            public void onError() {
                fingerprintAuthenticationDialogFragment.dismiss();
                Toast.makeText(context, getString(R.string.finger_error), Toast.LENGTH_SHORT).show();
            }
        });
        fingerprintAuthenticationDialogFragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    private void checkSwitchStatusAndOpenAuth(boolean isCheck) {
        int type = 0;
        if(isCheck) {
            type = AuthPinActivity.ADD_AUTH_TYPE;
        }else {
            type = AuthPinActivity.CLOSE_AUTH_TYPE;
        }
        openAuthActivity(type);
    }

    private void openAuthActivity(int type) {
        Intent intent = new Intent(this, AuthPinActivity.class);
        intent.putExtra(C.EXTRA_AUTH_TYPE, type);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
