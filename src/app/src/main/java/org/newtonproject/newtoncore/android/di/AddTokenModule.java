package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.AddTokenInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.TokenRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.MyTokensRouter;
import org.newtonproject.newtoncore.android.viewmodels.AddTokenViewModelFactory;

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
public class AddTokenModule {

    @Provides
    AddTokenViewModelFactory addTokenViewModelFactory(
            AddTokenInteract addTokenInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            MyTokensRouter myTokensRouter) {
        return new AddTokenViewModelFactory(
                addTokenInteract, findDefaultWalletInteract, myTokensRouter);
    }

    @Provides
    AddTokenInteract provideAddTokenInteract(
            TokenRepositoryType tokenRepository,
            WalletRepositoryType walletRepository) {
        return new AddTokenInteract(walletRepository, tokenRepository);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    MyTokensRouter provideMyTokensRouter() {
        return new MyTokensRouter();
    }
}
