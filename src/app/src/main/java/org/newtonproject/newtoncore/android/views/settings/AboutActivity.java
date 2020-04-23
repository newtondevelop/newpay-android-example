package org.newtonproject.newtoncore.android.views.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.views.intro.WebViewActivity;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.newtonproject.newtoncore.android.viewmodels.AboutViewModel;
import org.newtonproject.newtoncore.android.viewmodels.AboutViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AboutActivity extends BaseActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();
    @BindView(R.id.checkUpdateTextView)
    TextView checkUpdateTextView;
    @BindView(R.id.userOfTermsTextView)
    TextView userOfTermsTextView;
    private TextView versionTextView;
    @Inject
    AboutViewModelFactory factory;
    AboutViewModel viewModel;
    private int versionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        toolbar();
        viewModel = ViewModelProviders.of(this, factory).get(AboutViewModel.class);
        versionTextView = findViewById(R.id.versionTextView);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode = pInfo.versionCode;
            versionTextView.setText(String.format("V%s-%s-%s", version, String.valueOf(versionCode), C.APP_ENV));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.userOfTermsTextView)
    public void onUserOfTermsTextViewClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(C.EXTRA_URL, C.USER_OF_TERMS_URL);
        intent.putExtra(C.EXTRA_TITLE, StringUtil.getString(R.string.user_terms));
        startActivity(intent);
    }
}
