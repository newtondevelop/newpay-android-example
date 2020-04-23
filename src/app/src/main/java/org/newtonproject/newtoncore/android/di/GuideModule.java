package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.GuideViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/12/29--7:06 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
public class GuideModule {

    @Provides
    public GuideViewModelFactory provideGuideViewModelFactory(PreferenceRepositoryType preferenceRepositoryType) {
        return new GuideViewModelFactory(preferenceRepositoryType);
    }
}
