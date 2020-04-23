package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ImportWalletInteract {

    private final WalletRepositoryType walletRepository;
    private final PasswordStore passwordStore;

    public ImportWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet> importKeystore(String keystore,String name, String password) {
        return Single.fromCallable(()-> password)
                .flatMap(newPassword -> walletRepository
                        .importKeystoreToWallet(keystore, name, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Wallet> importPrivateKey(String privateKey, String name, String password) {
        return Single.fromCallable(() -> password)
                .flatMap(newPassword -> walletRepository
                        .importPrivateKeyToWallet(privateKey, name, newPassword))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Wallet> importMnemonic(List<String> mnemonics,String name, String password) {
        return Single.fromCallable(() -> password)
                .flatMap(newPassword -> walletRepository
                        .importMnemonicToWallet(mnemonics, name, newPassword))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable checkKeystore(String keyStore, String password) {
        return walletRepository.checkKeystore(keyStore, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Wallet> importKeystoreWithNewPassword(String keystore, String oldPassword, String newpassword) {
        return walletRepository
                .importKeystore(keystore, oldPassword, newpassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
