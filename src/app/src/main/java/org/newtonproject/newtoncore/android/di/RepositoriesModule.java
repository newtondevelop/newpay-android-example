package org.newtonproject.newtoncore.android.di;

import android.content.Context;

import com.google.gson.Gson;

import org.newtonproject.newtoncore.android.data.repository.NetworkRepository;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.RealmTokenSource;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.data.repository.TokenLocalSource;
import org.newtonproject.newtoncore.android.data.repository.TokenRepository;
import org.newtonproject.newtoncore.android.data.repository.TokenRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionInDiskSource;
import org.newtonproject.newtoncore.android.data.repository.TransactionInMemorySource;
import org.newtonproject.newtoncore.android.data.repository.TransactionLocalSource;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepository;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepository;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.data.service.AccountKeystoreService;
import org.newtonproject.newtoncore.android.data.service.BlockExplorerClient;
import org.newtonproject.newtoncore.android.data.service.BlockExplorerClientType;
import org.newtonproject.newtoncore.android.data.service.EthplorerTokenService;
import org.newtonproject.newtoncore.android.data.service.NewKeystoreAccountService;
import org.newtonproject.newtoncore.android.data.service.TokenExplorerClientType;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
@Module
public class RepositoriesModule {
	@Singleton
	@Provides
    PreferenceRepositoryType providePreferenceRepository(Context context) {
		return new SharedPreferenceRepository(context);
	}

	@Singleton
	@Provides
    AccountKeystoreService provideAccountKeyStoreService(Context context) {
        File file = new File(context.getFilesDir(), "keystore/keystore/");
		return new NewKeystoreAccountService(file);
	}

	@Singleton
	@Provides
    NetworkRepositoryType provideEthereumNetworkRepository(
            PreferenceRepositoryType preferenceRepository) {
		return new NetworkRepository(preferenceRepository);
	}

	@Singleton
	@Provides
    WalletRepositoryType provideWalletRepository(
            OkHttpClient okHttpClient,
			PreferenceRepositoryType preferenceRepositoryType,
			AccountKeystoreService accountKeystoreService,
			NetworkRepositoryType networkRepository) {
		return new WalletRepository(
		        okHttpClient, preferenceRepositoryType, accountKeystoreService, networkRepository);
	}

	@Singleton
	@Provides
    TransactionRepositoryType provideTransactionRepository(
			NetworkRepositoryType networkRepository,
			AccountKeystoreService accountKeystoreService,
			BlockExplorerClientType blockExplorerClient) {
		TransactionLocalSource inMemoryCache = new TransactionInMemorySource();
		TransactionLocalSource inDiskCache = new TransactionInDiskSource();
		return new TransactionRepository(
				networkRepository,
				accountKeystoreService,
				inMemoryCache,
				inDiskCache,
				blockExplorerClient);
	}

	@Singleton
	@Provides
    BlockExplorerClientType provideBlockExplorerClient(
			OkHttpClient httpClient,
			Gson gson,
			NetworkRepositoryType networkRepositoryType
			) {
		return new BlockExplorerClient(httpClient, gson, networkRepositoryType);
	}

	@Singleton
    @Provides
    TokenRepositoryType provideTokenRepository(
            OkHttpClient okHttpClient,
            NetworkRepositoryType ethereumNetworkRepository,
            TokenExplorerClientType tokenExplorerClientType,
            TokenLocalSource tokenLocalSource) {
	    return new TokenRepository(
	            okHttpClient,
	            ethereumNetworkRepository,
	            tokenExplorerClientType,
                tokenLocalSource);
    }

	@Singleton
    @Provides
    TokenExplorerClientType provideTokenService(OkHttpClient okHttpClient, Gson gson) {
	    return new EthplorerTokenService(okHttpClient, gson);
    }

    @Singleton
    @Provides
    TokenLocalSource provideRealmTokenSource() {
	    return new RealmTokenSource();
    }

}
