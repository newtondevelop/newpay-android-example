package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.MyAddressRouter;
import org.newtonproject.newtoncore.android.router.ScanRouter;
import org.newtonproject.newtoncore.android.router.SendRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
import org.newtonproject.newtoncore.android.viewmodels.home.HomeViewModelFactory;

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
public class HomeModule {

    @Provides
    HomeViewModelFactory provideHomeViewModelFactory(FetchWalletsInteract fetchWalletsInteract,
                                                     MyAddressRouter myAddressRouter,
                                                     SendRouter sendRouter,
                                                     GetDefaultWalletBalance getDefaultWalletBalance,
                                                     TransactionsRouter transactionsRouter,
                                                     ScanRouter scanRouter,
                                                     PreferenceRepositoryType preferenceRepositoryType,
                                                     NetworkRepositoryType networkRepositoryType,
                                                     CreateTransactionInteract createTransactionInteract
                                                     ) {
        return new HomeViewModelFactory(fetchWalletsInteract, myAddressRouter, sendRouter,
                getDefaultWalletBalance, transactionsRouter, scanRouter,
                preferenceRepositoryType,
                networkRepositoryType,
                createTransactionInteract);
    }


    @Provides
    FetchWalletsInteract provideFetchWalletsInteract(WalletRepositoryType walletRepository) {
        return new FetchWalletsInteract(walletRepository);
    }

    @Provides
    MyAddressRouter provideMyAddressRouter() {
        return new MyAddressRouter();
    }

    @Provides
    SendRouter provideSendRouter() {
        return new SendRouter();
    }

    @Provides
    TransactionsRouter provideTransactionRouter() { return new TransactionsRouter(); }

    @Provides
    ScanRouter provideScanRouter() { return new ScanRouter(); }

    @Provides
    GetDefaultWalletBalance provideGetDefaultWalletBalance(WalletRepositoryType walletRepositoryType, NetworkRepositoryType networkRepositoryType, PreferenceRepositoryType preferenceRepositoryType) {
        return new GetDefaultWalletBalance(walletRepositoryType, networkRepositoryType, preferenceRepositoryType);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType repositoryType) {
        return new CreateTransactionInteract(repositoryType);
    }
}
