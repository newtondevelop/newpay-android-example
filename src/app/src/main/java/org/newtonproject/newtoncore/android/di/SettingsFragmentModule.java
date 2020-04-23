package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.ManageWalletsRouter;

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
class SettingsFragmentModule {
    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    ManageWalletsRouter provideManageWalletsRouter() {
        return new ManageWalletsRouter();
    }
}
