package org.newtonproject.newtoncore.android.di;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.newtonproject.newtoncore.android.App;
import org.newtonproject.newtoncore.android.data.repository.NewPasswordStore;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.utils.LogInterceptor;
import org.newtonproject.newtoncore.android.utils.StringUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
class ToolsModule {
	@Provides
	Context provideContext(App application) {
		return application.getApplicationContext();
	}

	@Singleton
	@Provides
	Gson provideGson() {
		return StringUtil.getGsonWithoutNull();
	}

	@Singleton
	@Provides
	OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .build();
	}

	@Singleton
	@Provides
	PasswordStore passwordStore(Context context) {
		return new NewPasswordStore();
	}

	@Singleton
	@Provides
	Picasso picasso(Context context, OkHttpClient okHttpClient) {
		return new Picasso.Builder(context)
				.downloader(new OkHttp3Downloader(okHttpClient))
				.build();
	}
}
