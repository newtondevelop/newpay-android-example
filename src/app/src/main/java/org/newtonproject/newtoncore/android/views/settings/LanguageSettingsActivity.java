package org.newtonproject.newtoncore.android.views.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.views.base.BaseActivity;
import org.newtonproject.newtoncore.android.utils.LanguageUtils;
import org.newtonproject.newtoncore.android.viewmodels.SwitchNetworkViewModel;
import org.newtonproject.newtoncore.android.viewmodels.SwitchNetworkViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class LanguageSettingsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LanguageSettings";
    @BindView(R.id.japan)
    LinearLayout japan;
    private LinearLayout chinese;
    private LinearLayout english;
    private SharedPreferenceRepository sharedPreferenceRepository;
    private int currentLanguage;
    @Inject
    SwitchNetworkViewModelFactory factory;
    SwitchNetworkViewModel viewModel;
    private boolean isFromGuide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        isFromGuide = intent.getBooleanExtra(C.LANGUAGE_SOURCE_IS_GUIDE, false);
        viewModel = ViewModelProviders.of(this, factory).get(SwitchNetworkViewModel.class);
        toolbar();
        initViews();
    }

    private void initViews() {
        sharedPreferenceRepository = new SharedPreferenceRepository(this);
        int sharePreferenceLanguageType = sharedPreferenceRepository.getSharePreferenceLanguageType();
        chinese = findViewById(R.id.chinese);
        english = findViewById(R.id.english);
        setCurrentLanguage(sharePreferenceLanguageType);
        chinese.setOnClickListener(this);
        english.setOnClickListener(this);
        japan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chinese:
                changeLanguage(C.LANGUAGE_CHINESE_INDEX);
                break;
            case R.id.english:
                changeLanguage(C.LANGUAGE_ENGLISH_INDEX);
                break;
            case R.id.japan:
                changeLanguage(C.LANGUAGE_JAPANESE_INDEX);
                break;
            default:
                break;
        }
    }

    private void changeLanguage(int languageType) {
        boolean isSameLanguage = LanguageUtils.isSameLanguage(this, languageType);
        Log.e(TAG, "changeLanguage: " + isSameLanguage);
        if (!isSameLanguage) {
            LanguageUtils.setLocale(this, languageType);
            viewModel.updateNetworkConfig();
            if(isFromGuide) {
                LanguageUtils.restartNewIdActvity(this);
            }else{
                LanguageUtils.restartActvity(this);
            }
            finish();
        }
    }

    public void setCurrentLanguage(int currentLanguage) {
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ticked));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(params);
        this.currentLanguage = currentLanguage;
        switch (currentLanguage) {
            case C.LANGUAGE_CHINESE_INDEX:
                chinese.addView(imageView);
                break;
            case C.LANGUAGE_ENGLISH_INDEX:
                english.addView(imageView);
                break;
            case C.LANGUAGE_JAPANESE_INDEX:
                japan.addView(imageView);
            default:
                break;
        }
    }
}
