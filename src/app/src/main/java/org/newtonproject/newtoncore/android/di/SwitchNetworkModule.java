package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.SwitchNetworkViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/10/20--11:00 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
public class SwitchNetworkModule {
    @Provides
    SwitchNetworkViewModelFactory provideSwitchNetworkViewModelFactory(NetworkRepositoryType networkRepositoryType,
                                                                       PreferenceRepositoryType preferenceRepositoryType) {
        return new SwitchNetworkViewModelFactory(networkRepositoryType, preferenceRepositoryType);
    }
}
