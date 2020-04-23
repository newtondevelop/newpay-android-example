package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.CreateWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.DeleteWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.ExportWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.SetDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.router.ImportWalletRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
import org.newtonproject.newtoncore.android.viewmodels.WalletsViewModelFactory;

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
public class CreateWalletModule {

    @Provides
    WalletsViewModelFactory provideCreateWalletViewModelFactory(
            CreateWalletInteract createWalletInteract,
            SetDefaultWalletInteract setDefaultWalletInteract,
            DeleteWalletInteract deleteWalletInteract,
            FetchWalletsInteract fetchWalletsInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExportWalletInteract exportWalletInteract,
            ImportWalletRouter importWalletRouter,
            TransactionsRouter transactionsRouter,
            PreferenceRepositoryType preferenceRepositoryType){
        return new WalletsViewModelFactory(createWalletInteract,
                setDefaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
                transactionsRouter,
                preferenceRepositoryType);
    }


    @Provides
    CreateWalletInteract provideCreateAccountInteract(
              WalletRepositoryType accountRepository, PasswordStore passwordStore) {
        return new CreateWalletInteract(accountRepository, passwordStore);
    }

    @Provides
    SetDefaultWalletInteract provideSetDefaultAccountInteract(WalletRepositoryType accountRepository) {
        return new SetDefaultWalletInteract(accountRepository);
    }

    @Provides
    DeleteWalletInteract provideDeleteAccountInteract(
              WalletRepositoryType accountRepository, TransactionRepositoryType transactionRepositoryType, PreferenceRepositoryType preferenceRepositoryType) {
        return new DeleteWalletInteract(accountRepository, transactionRepositoryType, preferenceRepositoryType);
    }

    @Provides
    FetchWalletsInteract provideFetchAccountsInteract(WalletRepositoryType accountRepository) {
        return new FetchWalletsInteract(accountRepository);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultAccountInteract(WalletRepositoryType accountRepository) {
        return new FindDefaultWalletInteract(accountRepository);
    }

    @Provides
    ExportWalletInteract provideExportWalletInteract(
              WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        return new ExportWalletInteract(walletRepository, passwordStore);
    }

    @Provides
    ImportWalletRouter provideImportAccountRouter() {
        return new ImportWalletRouter();
    }

    @Provides
    TransactionsRouter provideTransactionsRouter() {
        return new TransactionsRouter();
    }
}
