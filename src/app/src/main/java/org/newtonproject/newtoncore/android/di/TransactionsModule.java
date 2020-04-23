package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.FetchTransactionsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.ExternalBrowserRouter;
import org.newtonproject.newtoncore.android.router.TransactionDetailRouter;
import org.newtonproject.newtoncore.android.viewmodels.TransactionsViewModelFactory;

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
class TransactionsModule {
    @Provides
    TransactionsViewModelFactory provideTransactionsViewModelFactory(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            GetDefaultWalletBalance getDefaultWalletBalance,
            TransactionDetailRouter transactionDetailRouter,
            ExternalBrowserRouter externalBrowserRouter,
            PreferenceRepositoryType preferenceRepositoryType
    ) {
        return new TransactionsViewModelFactory(
                findDefaultNetworkInteract,
                findDefaultWalletInteract,
                fetchTransactionsInteract,
                getDefaultWalletBalance,
                transactionDetailRouter,
                externalBrowserRouter,
                preferenceRepositoryType);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            NetworkRepositoryType networkRepositoryType) {
        return new FindDefaultNetworkInteract(networkRepositoryType);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionsInteract(TransactionRepositoryType transactionRepository) {
        return new FetchTransactionsInteract(transactionRepository);
    }

    @Provides
    GetDefaultWalletBalance provideGetDefaultWalletBalance(
            WalletRepositoryType walletRepository, NetworkRepositoryType ethereumNetworkRepository, PreferenceRepositoryType preferenceRepositoryType) {
        return new GetDefaultWalletBalance(walletRepository, ethereumNetworkRepository, preferenceRepositoryType);
    }

    @Provides
    TransactionDetailRouter provideTransactionDetailRouter() {
        return new TransactionDetailRouter();
    }

    @Provides
    ExternalBrowserRouter provideExternalBrowserRouter() {
        return new ExternalBrowserRouter();
    }

}
