package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.PasswordStore;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ExportWalletInteract {

    private final WalletRepositoryType walletRepository;
    private final PasswordStore passwordStore;

    public ExportWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<String> export(Wallet wallet, String transactionPassword) {
        return Single.fromCallable(()-> transactionPassword)
                .flatMap(walletpassword -> walletRepository
                    .exportWallet(wallet, walletpassword))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<String> exportPrivateKey(Wallet wallet, String transactionPassword) {
        return Single.fromCallable(()-> transactionPassword)
                .flatMap(password -> walletRepository
                .exportWalletPrivateKey(wallet, password))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<String>> exportMnemonic(Wallet wallet, String transactionPassword) {
        return Single.fromCallable(()-> transactionPassword)
                .flatMap(password -> walletRepository
                .exportWalletMnemonic(wallet, password))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
