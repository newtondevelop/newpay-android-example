package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.FetchTokensInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TokenRepositoryType;
import org.newtonproject.newtoncore.android.router.AddTokenRouter;
import org.newtonproject.newtoncore.android.router.SendTokenRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
import org.newtonproject.newtoncore.android.viewmodels.TokensViewModelFactory;

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
class TokensModule {

    @Provides
    TokensViewModelFactory provideTokensViewModelFactory(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FetchTokensInteract fetchTokensInteract,
            AddTokenRouter addTokenRouter,
            SendTokenRouter sendTokenRouter,
            TransactionsRouter transactionsRouter) {
        return new TokensViewModelFactory(
                findDefaultNetworkInteract,
                fetchTokensInteract,
                addTokenRouter,
                sendTokenRouter,
                transactionsRouter);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            NetworkRepositoryType networkRepository) {
        return new FindDefaultNetworkInteract(networkRepository);
    }

    @Provides
    FetchTokensInteract provideFetchTokensInteract(TokenRepositoryType tokenRepository) {
        return new FetchTokensInteract(tokenRepository);
    }

    @Provides
    AddTokenRouter provideAddTokenRouter() {
        return new AddTokenRouter();
    }

    @Provides
    SendTokenRouter provideSendTokenRouter() {
        return new SendTokenRouter();
    }

    @Provides
    TransactionsRouter provideTransactionsRouter() {
        return new TransactionsRouter();
    }
}
