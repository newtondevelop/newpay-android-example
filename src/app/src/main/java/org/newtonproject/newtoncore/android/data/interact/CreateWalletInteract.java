package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;

import java.util.ArrayList;

import io.reactivex.Single;

import static org.newtonproject.newtoncore.android.data.interact.rx.operator.Operators.completableErrorProxy;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class CreateWalletInteract {

	private final WalletRepositoryType walletRepository;
	private final PasswordStore passwordStore;
	private final String TAG = "CreateWalletInteract";
	public CreateWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
		this.walletRepository = walletRepository;
		this.passwordStore = passwordStore;
	}

	public Single<Wallet> create(String passwrod) {
		Single<Wallet> res = Single.fromCallable(() -> passwrod)
				.flatMap(masterPassword -> walletRepository
						.createWallet(masterPassword)
						.flatMap(wallet -> passwordVerification(wallet, masterPassword)));
	    return res;
	}

	private Single<Wallet> passwordVerification(Wallet wallet, String masterPassword) {
            return Single.fromCallable(() -> masterPassword)
                .flatMap(password -> walletRepository
                        .exportWallet(wallet, password)
                        .flatMap(keyStore -> walletRepository.findWallet(wallet.address)))
                .onErrorResumeNext(throwable -> walletRepository
                        .deleteWallet(wallet.address, masterPassword)
                        .lift(completableErrorProxy(throwable))
                        .toSingle(() -> wallet));
	}

	public Single<ArrayList<String>> createMnemonic() {
		return walletRepository.createWalletMnemonic();
	}

	public Single<Wallet> createWalletDirect(String password, String name) {
		return walletRepository.createWalletMnemonic()
				.flatMap(mnemonic -> walletRepository.importMnemonicToWallet(mnemonic, name, password));
	}

}
