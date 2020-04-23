package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.router.AboutRouter;
import org.newtonproject.newtoncore.android.router.FriendsRouter;
import org.newtonproject.newtoncore.android.router.SettingsRouter;
import org.newtonproject.newtoncore.android.router.WalletsListRouter;
import org.newtonproject.newtoncore.android.viewmodels.MeFragmentViewModelFactory;

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
public class MeModule {

    @Provides
    MeFragmentViewModelFactory provideMeFragmentViewModeFactory(WalletsListRouter walletsListRouter,
                                                                FriendsRouter friendsRouter,
                                                                SettingsRouter settingsRouter,
                                                                PreferenceRepositoryType preferenceRepositoryType,
                                                                CreateTransactionInteract createTransactionInteract) {
        return new MeFragmentViewModelFactory(walletsListRouter, friendsRouter, settingsRouter, preferenceRepositoryType, createTransactionInteract);
    }

    @Provides
    WalletsListRouter provideWalletListRouter() {
        return new WalletsListRouter();
    }

    @Provides
    FriendsRouter provideFriendsRouter() {
        return new FriendsRouter();
    }

    @Provides
    SettingsRouter provideSettingsRouter() {
        return new SettingsRouter();
    }

    @Provides
    AboutRouter provideAboutRouter() {
        return new AboutRouter();
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepositoryType) {
        return new CreateTransactionInteract(transactionRepositoryType);
    }
}
