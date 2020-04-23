package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchGasSettingsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.GasSettingsRouter;
import org.newtonproject.newtoncore.android.router.ScanRouter;
import org.newtonproject.newtoncore.android.viewmodels.SendViewModelFactory;

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
class SendModule {
    @Provides
    SendViewModelFactory provideSendViewModelFactory(ScanRouter scanRouter, GasSettingsRouter gasSettingsRouter,
                                                     FindDefaultWalletInteract findDefaultWalletInteract,
                                                     FetchGasSettingsInteract fetchGasSettingsInteract,
                                                     CreateTransactionInteract createTransactionInteract,
                                                     PreferenceRepositoryType preferenceRepositoryType,
                                                     GetDefaultWalletBalance getDefaultWalletBalance
                                                      ) {
        return new SendViewModelFactory(scanRouter, gasSettingsRouter, findDefaultWalletInteract, fetchGasSettingsInteract,
                createTransactionInteract, preferenceRepositoryType, getDefaultWalletBalance);
    }

    @Provides
    GasSettingsRouter provideGasSettingsRouter() {
        return new GasSettingsRouter();
    }

    @Provides
    ScanRouter provideScanRouter() { return new ScanRouter(); }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepositoryType) {
        return new FindDefaultWalletInteract(walletRepositoryType);
    }

    @Provides
    FetchGasSettingsInteract provideFetchGasSettingsInteract (WalletRepositoryType walletRepositoryType) {
        return new FetchGasSettingsInteract(walletRepositoryType);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepositoryType) {
        return new CreateTransactionInteract(transactionRepositoryType);
    }

    @Provides
    GetDefaultWalletBalance provideGetDefaultWalletBalance(WalletRepositoryType walletRepositoryType, NetworkRepositoryType networkRepositoryType, PreferenceRepositoryType preferenceRepositoryType) {
        return new GetDefaultWalletBalance(walletRepositoryType, networkRepositoryType, preferenceRepositoryType);
    }
}
