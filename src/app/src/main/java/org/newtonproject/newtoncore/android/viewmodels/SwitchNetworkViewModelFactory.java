package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/10/20--10:52 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SwitchNetworkViewModelFactory implements ViewModelProvider.Factory {

    private final NetworkRepositoryType networkRepositoryType;
    private final PreferenceRepositoryType preferenceRepositoryType;

    public SwitchNetworkViewModelFactory(NetworkRepositoryType networkRepositoryType,
                                         PreferenceRepositoryType preferenceRepositoryType) {
        this.networkRepositoryType = networkRepositoryType;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SwitchNetworkViewModel(networkRepositoryType, preferenceRepositoryType);
    }
}
