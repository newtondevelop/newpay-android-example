package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class DeleteWalletInteract {
	private final WalletRepositoryType walletRepository;
	private final TransactionRepositoryType transactionRepositoryType;
	private PreferenceRepositoryType mpreference;

	public DeleteWalletInteract(WalletRepositoryType walletRepository,
								TransactionRepositoryType transactionRepositoryType,
								PreferenceRepositoryType preferenceRepositoryType) {
		this.walletRepository = walletRepository;
		this.transactionRepositoryType = transactionRepositoryType;
		this.mpreference = preferenceRepositoryType;
	}

	public Single<Wallet[]> delete(Wallet wallet, String transactionPassword) {
		return Single.fromCallable(()-> transactionPassword)
				.flatMapCompletable(password ->
					walletRepository.deleteWallet(wallet.address, password))
				.andThen(Completable.fromCallable(() -> {
					transactionRepositoryType.clearTransaction(wallet);
					mpreference.clearPreference();
					return null;
				}))
				.andThen(walletRepository.fetchWallets())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
