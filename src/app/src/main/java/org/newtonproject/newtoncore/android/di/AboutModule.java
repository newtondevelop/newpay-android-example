package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.AboutViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
public class AboutModule {

    @Provides
    AboutViewModelFactory provideAboutViewModelFactory( PreferenceRepositoryType preferenceRepositoryType) {
        return new AboutViewModelFactory(preferenceRepositoryType);
    }

}
