package org.newtonproject.newtoncore.android.di;


import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.GasSettingsViewModelFactory;

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
public class GasSettingsModule {

    @Provides
    public GasSettingsViewModelFactory provideGasSettingsViewModelFactory(FindDefaultNetworkInteract findDefaultNetworkInteract) {
        return new GasSettingsViewModelFactory(findDefaultNetworkInteract);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            NetworkRepositoryType networkRepositoryType) {
        return new FindDefaultNetworkInteract(networkRepositoryType);
    }
}
