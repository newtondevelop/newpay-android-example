package org.newtonproject.newtoncore.android.views.account;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.rd.PageIndicatorView;

import org.newtonproject.newtoncore.android.BuildConfig;
import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.router.CreateWalletRouter;
import org.newtonproject.newtoncore.android.router.HomeRouter;
import org.newtonproject.newtoncore.android.router.ImportWalletRouter;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.settings.LanguageSettingsActivity;
import org.newtonproject.newtoncore.android.views.widget.adapter.IntroPagerAdapter;
import org.newtonproject.newtoncore.android.utils.CheckUtils;
import org.newtonproject.newtoncore.android.utils.LanguageUtils;
import org.newtonproject.newtoncore.android.utils.ToastUtils;
import org.newtonproject.newtoncore.android.utils.UIToolkit;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static org.newtonproject.newtoncore.android.C.REQUEST_CODE_IMPORT_WALLET;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WelcomeActivity extends BaseImplActivity {
    private String TAG = "WelcomeActivity";
    private File apkfile;
    private double exitTime;

    @BindView(R.id.languageTextView)
    TextView languageTextView;
    @BindView(R.id.versionTextView)
    TextView versionTextView;
    private SharedPreferenceRepository preferenceRepositoryType;

    @Override
    protected void initView() {
        preferenceRepositoryType = new SharedPreferenceRepository(this);
        ViewPager viewPager = findViewById(R.id.viewPager);
        versionTextView.setText(String.format("Version: %s-%s", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE + ""));
        IntroPagerAdapter adapter = new IntroPagerAdapter();
        viewPager.setPageTransformer(false, new IntroPagerAdapter.DepthPageTransformer());
        viewPager.setAdapter(adapter);
        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(viewPager);
        CheckUtils.checkPermissionForApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String currentLanguage = LanguageUtils.getCurrentLanguage(mContext);
        languageTextView.setText(String.format("Language: %s >", currentLanguage));
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViewModel() {

    }

    @OnClick({R.id.languageTextView,
                R.id.action_create,
                R.id.action_import
                })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_create:
                new CreateWalletRouter().open(this, false);
                break;
            case R.id.action_import:
                new ImportWalletRouter().openForResult(this, REQUEST_CODE_IMPORT_WALLET);
                break;
            case R.id.languageTextView:
                Intent intent = new Intent(this, LanguageSettingsActivity.class);
                intent.putExtra(C.LANGUAGE_SOURCE_IS_GUIDE, true);
                startActivity(intent);
            default:
                break;
        }
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
        if(requestCode == C.REQUEST_CODE_GET_UNKNOWN_APP_SOURCES){
            if(apkfile != null && Build.VERSION.SDK_INT >= 26) {
                boolean b = getPackageManager().canRequestPackageInstalls();
                if (b) {
                    UIToolkit.installApk(this, apkfile);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, C.PERMISSION_CODE_INSTALL_PACKAGES);
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, C.REQUEST_CODE_GET_UNKNOWN_APP_SOURCES);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case C.PERMISSION_CODE_INSTALL_PACKAGES:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if(apkfile != null) {
                        UIToolkit.installApk(this, apkfile);
                    }
                }
                break;
            case C.PERMISSION_CODE_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if(tm == null) return;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String imei = tm.getDeviceId();
                    if (!TextUtils.isEmpty(imei)) {
                        new SharedPreferenceRepository(this).setIMEI(imei);
                    } else {
                        if(TextUtils.isEmpty(preferenceRepositoryType.getIMEI())) {
                            preferenceRepositoryType.setIMEI(StringUtil.getNonce());
                        }
                    }
                }
                break;
        }
    }

    public void setInstallFile(File apkfile) {
        this.apkfile = apkfile;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showNormalToast(this, getString(R.string.exit));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
