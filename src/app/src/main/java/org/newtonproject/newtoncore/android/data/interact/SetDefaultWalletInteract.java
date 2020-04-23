package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SetDefaultWalletInteract {

	private WalletRepositoryType accountRepository;

	public SetDefaultWalletInteract(WalletRepositoryType walletRepositoryType) {
		this.accountRepository = walletRepositoryType;
	}

	public Completable set(Wallet wallet) {
		return accountRepository
				.setDefaultWallet(wallet)
				.observeOn(AndroidSchedulers.mainThread());
	}
}
