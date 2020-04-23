package org.newtonproject.newtoncore.android.di;

import org.newtonproject.newtoncore.android.data.interact.ImportWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.viewmodels.ImportWalletViewModelFactory;

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
class ImportModule {
    @Provides
    ImportWalletViewModelFactory provideImportWalletViewModelFactory(
            ImportWalletInteract importWalletInteract,
            PreferenceRepositoryType preferenceRepositoryType) {
        return new ImportWalletViewModelFactory(importWalletInteract, preferenceRepositoryType);
    }

    @Provides
    ImportWalletInteract provideImportWalletInteract(
            WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        return new ImportWalletInteract(walletRepository, passwordStore);
    }
}
