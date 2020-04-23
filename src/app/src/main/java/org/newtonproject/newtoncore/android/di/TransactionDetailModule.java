package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.FetchTransactionsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.ExternalBrowserRouter;
import org.newtonproject.newtoncore.android.viewmodels.TransactionDetailViewModelFactory;

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
public class TransactionDetailModule {

    @Provides
    TransactionDetailViewModelFactory provideTransactionDetailViewModelFactory(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExternalBrowserRouter externalBrowserRouter,
            FetchTransactionsInteract fetchTransactionsInteract) {
        return new TransactionDetailViewModelFactory(
                findDefaultNetworkInteract, findDefaultWalletInteract, externalBrowserRouter, fetchTransactionsInteract);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            NetworkRepositoryType ethereumNetworkRepository) {
        return new FindDefaultNetworkInteract(ethereumNetworkRepository);
    }

    @Provides
    ExternalBrowserRouter externalBrowserRouter() {
        return new ExternalBrowserRouter();
    }

    @Provides
    FindDefaultWalletInteract findDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionInteract(TransactionRepositoryType transactionRepositoryType) {
        return new FetchTransactionsInteract(transactionRepositoryType);
    }
}
