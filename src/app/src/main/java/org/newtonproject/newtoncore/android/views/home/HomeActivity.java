package org.newtonproject.newtoncore.android.views.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.utils.CheckUtils;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.utils.ToastUtils;
import org.newtonproject.newtoncore.android.utils.UIToolkit;
import org.newtonproject.newtoncore.android.utils.ViewUtils;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.views.widget.adapter.TabPagerAdapter;
import org.newtonproject.newtoncore.android.widget.NoScrollerViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.mainViewPager)
    NoScrollerViewPager mainViewPager;
    @BindView(R.id.walletImageView)
    ImageView walletImageView;
    @BindView(R.id.walletTextView)
    TextView walletTextView;
    @BindView(R.id.walletLinearLayout)
    LinearLayout walletLinearLayout;
    @BindView(R.id.meImageView)
    ImageView meImageView;
    @BindView(R.id.meTextView)
    TextView meTextView;
    @BindView(R.id.meLinearLayout)
    LinearLayout meLinearLayout;
    @BindView(R.id.bottomLayout)
    LinearLayout bottomLayout;

    // view pager pages
    private List<Pair<String, Fragment>> pages = new ArrayList<>();

    private final static int HOME_INDEX = 0;
    private final static int ME_INDEX = 1;

    private File apkfile;
    private double exitTime;
    private SharedPreferenceRepository preferenceRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initViews();
        preferenceRepository = new SharedPreferenceRepository(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        BitmapDrawable blurBackground = ViewUtils.getBlurBackground();
        bottomLayout.setBackground(blurBackground);
        HomeFragment homeFragment = new HomeFragment();
        MeFragment meFragment = new MeFragment();

        pages.add(HOME_INDEX, new Pair<>(getString(R.string.title_home), homeFragment));
        pages.add(ME_INDEX, new Pair<>(getString(R.string.title_me), meFragment));

        mainViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), pages));
        mainViewPager.addOnPageChangeListener(this);
        mainViewPager.setOffscreenPageLimit(2);

        // home model datas
        walletLinearLayout.setOnClickListener(this);
        meLinearLayout.setOnClickListener(this);

        CheckUtils.checkPermissionForApp(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void updatePage(int meIndex) {
        mainViewPager.setCurrentItem(meIndex, false);
    }

    private void updateTab(int currentIndex) {
        updateHome(currentIndex);
        updateMe(currentIndex);
    }

    private void updateHome(int currentIndex) {
        if (currentIndex == HOME_INDEX) {
            walletImageView.setImageDrawable(getResources().getDrawable(R.mipmap.tab_icon_pressed_wallet));
            walletTextView.setTextColor(getResources().getColor(R.color.home_tab_pressed));
        } else {
            walletImageView.setImageDrawable(getResources().getDrawable(R.mipmap.tab_icon_normal_waleet));
            walletTextView.setTextColor(getResources().getColor(R.color.home_tab_normal));
        }
    }

    private void updateMe(int currentIndex) {
        if (currentIndex == ME_INDEX) {
            meImageView.setImageDrawable(getResources().getDrawable(R.mipmap.tab_icon_pressed_me));
            meTextView.setTextColor(getResources().getColor(R.color.home_tab_pressed));
        } else {
            meImageView.setImageDrawable(getResources().getDrawable(R.mipmap.tab_icon_normal_me));
            meTextView.setTextColor(getResources().getColor(R.color.home_tab_normal));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result) {
            String extracted_address = result.getContents();
            if (null != extracted_address) {
                Logger.e(TAG, extracted_address);
            } else {
                Toast.makeText(this, getString(R.string.no_address_scaned), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == C.REQUEST_CODE_GET_UNKNOWN_APP_SOURCES) {
            if (apkfile != null && Build.VERSION.SDK_INT >= 26) {
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
                    if (apkfile != null) {
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
                        preferenceRepository.setIMEI(imei);
                    }
                } else {
                    if(TextUtils.isEmpty(preferenceRepository.getIMEI())) {
                        preferenceRepository.setIMEI(StringUtil.getNonce());
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int currentItem = mainViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.walletLinearLayout:
//                ViewUtils.addFlag(C.RECORD.HOME_TAB);
                if (currentItem == HOME_INDEX) return;
                updatePage(HOME_INDEX);
                break;
            case R.id.meLinearLayout:
//                ViewUtils.addFlag(C.RECORD.ME_TAB);
                if (currentItem == ME_INDEX) return;
                updatePage(ME_INDEX);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
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

    public void setInstallFile(File apkfile) {
        this.apkfile = apkfile;
    }
}
