package org.newtonproject.newtoncore.android.di;


import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchGasSettingsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.GasSettingsRouter;
import org.newtonproject.newtoncore.android.viewmodels.ConfirmationViewModelFactory;

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
public class ConfirmationModule {
    @Provides
    public ConfirmationViewModelFactory provideConfirmationViewModelFactory(
            FindDefaultWalletInteract findDefaultWalletInteract,
            FetchGasSettingsInteract fetchGasSettingsInteract,
            CreateTransactionInteract createTransactionInteract,
            GasSettingsRouter gasSettingsRouter
    ) {
        return new ConfirmationViewModelFactory(findDefaultWalletInteract, fetchGasSettingsInteract, createTransactionInteract, gasSettingsRouter);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    FetchGasSettingsInteract provideFetchGasSettingsInteract(WalletRepositoryType walletRepositoryType) {
        return new FetchGasSettingsInteract(walletRepositoryType);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepository, PasswordStore passwordStore) {
        return new CreateTransactionInteract(transactionRepository, passwordStore);
    }

    @Provides
    GasSettingsRouter provideGasSettingsRouter() {
        return new GasSettingsRouter();
    }
}
