package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.UpdateWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.UpdatePasswordModelFactory;

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
public class UpdatePasswordModule {

    @Provides
    UpdatePasswordModelFactory provideUpdatePasswordModelFactory(
            UpdateWalletInteract updateWalletInteract, FetchWalletsInteract fetchWalletsInteract
    ) {
        return new UpdatePasswordModelFactory(updateWalletInteract, fetchWalletsInteract);
    }

    @Provides
    UpdateWalletInteract providerUpdateWalletInteract(
            WalletRepositoryType walletRepository
    ) {
        return new UpdateWalletInteract(walletRepository);
    }

    @Provides
    FetchWalletsInteract providerFetchWalletsInteract(
            WalletRepositoryType walletRepository
    ) {
        return new FetchWalletsInteract(walletRepository);
    }
}
