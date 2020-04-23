package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Singleton
@Component(modules = {
		AndroidSupportInjectionModule.class,
		ToolsModule.class,
		RepositoriesModule.class,
		BuildersModule.class })
public interface AppComponent {

	@Component.Builder
	interface Builder {
		@BindsInstance
		Builder application(App app);
		AppComponent build();
	}
	void inject(App app);
}
