package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.router.FriendsRouter;
import org.newtonproject.newtoncore.android.router.SettingsRouter;
import org.newtonproject.newtoncore.android.router.WalletsListRouter;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MeFragmentViewModelFactory implements ViewModelProvider.Factory {

    private WalletsListRouter walletsListRouter;
    private FriendsRouter friendsRouter;
    private SettingsRouter settingsRouter;
    private PreferenceRepositoryType preferenceRepositoryType;
    private CreateTransactionInteract createTransactionInteract;
    public MeFragmentViewModelFactory(WalletsListRouter walletsListRouter,
                                      FriendsRouter friendsRouter,
                                      SettingsRouter settingsRouter,
                                      PreferenceRepositoryType preferenceRepositoryType,
                                      CreateTransactionInteract createTransactionInteract) {
        this.walletsListRouter = walletsListRouter;
        this.friendsRouter = friendsRouter;
        this.settingsRouter = settingsRouter;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.createTransactionInteract = createTransactionInteract;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MeFragmentViewModel(
                walletsListRouter,
                friendsRouter,
                settingsRouter,
                preferenceRepositoryType,
                createTransactionInteract);
    }
}
