package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final FetchWalletsInteract fetchWalletsInteract;
    private final PreferenceRepositoryType preferenceRepositoryType;
    public SplashViewModelFactory(FetchWalletsInteract fetchWalletsInteract,
                                  PreferenceRepositoryType preferenceRepositoryType
                                  ) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SplashViewModel(fetchWalletsInteract, preferenceRepositoryType);
    }
}
