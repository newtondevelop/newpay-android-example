package org.newtonproject.newtoncore.android;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import org.newtonproject.newtoncore.android.data.manager.StatusManager;
import org.newtonproject.newtoncore.android.di.DaggerAppComponent;
import org.newtonproject.newtoncore.android.utils.LanguageUtils;
import org.newtonproject.newtoncore.android.utils.Logger;

import java.io.File;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.fabric.sdk.android.Fabric;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class App extends  MultiDexApplication implements HasActivityInjector, HasSupportFragmentInjector {

	private static final String TAG = "App";
	private static Context baseApplication;
	@Inject
	DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
	@Inject
	DispatchingAndroidInjector<Fragment> dispatchingAndroidFragmentInjector;

	@Override
	public void onCreate() {
		super.onCreate();
		initWebView();
        Realm.init(this);
        DaggerAppComponent
				.builder()
				.application(this)
				.build()
				.inject(this);
		baseApplication = this;
		LanguageUtils.setLocale(this);
		initCrash();
		initDir();
		initFont();
		initUserAgent();
		initRxJava();
	}

	private void initRxJava() {
		RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
			@Override
			public void accept(Throwable throwable) throws Exception {
				Logger.e(throwable.getMessage());
			}
		});
	}

	private void initUserAgent() {
		StatusManager.getInstance().initUserAgent(baseApplication);
	}

	private void initFont() {
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}

	private void initDir() {
		File cacheFile = new File(C.HEP_CACHE);
		if(!cacheFile.exists()) {
			cacheFile.mkdir();
		}
		File contact = new File(C.CONTACT_DIR);
		if(!contact.exists()) {
			contact.mkdir();
		}
		File transactionCacheFile = new File(C.TRANSACTION_CACHE);
		if(!transactionCacheFile.exists()) {
			transactionCacheFile.mkdir();
		}
	}

	private void initCrash() {
		Fabric.with(this, new Crashlytics.Builder()
				.core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build(), new Crashlytics());
	}

//	private void initFeedback() {
//		FeedbackAPI.init(this, C.ALIBABAKEY, C.ALIBABASECRET);
//	}


	@Override
	public AndroidInjector<Activity> activityInjector() {
		return dispatchingAndroidInjector;
	}

	public static Context getAppContext() {
		return baseApplication;
	}

	@Override
	public AndroidInjector<Fragment> supportFragmentInjector() {
		return dispatchingAndroidFragmentInjector;
	}

	private void initWebView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			String processName = getProcessName();
			Logger.e("process name:" + processName);
			if (!getPackageName().equals(processName)) {
				WebView.setDataDirectorySuffix(processName);
			}
		}
	}
}
