package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.WalletsListModelFactory;

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
public class WalletsListModule {
    @Provides
    WalletsListModelFactory provideWalletsListModelFactory(FetchWalletsInteract fetchWalletsInteract) {
        return new WalletsListModelFactory(fetchWalletsInteract);
    }

    @Provides
    FetchWalletsInteract provideFetchWalletInteract(WalletRepositoryType walletRepositoryType) {
        return new FetchWalletsInteract(walletRepositoryType);
    }
}
