package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AboutViewModelFactory implements ViewModelProvider.Factory {

    PreferenceRepositoryType preferenceRepositoryType;

    public AboutViewModelFactory( PreferenceRepositoryType preferenceRepositoryType) {
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AboutViewModel(preferenceRepositoryType);
    }
}
