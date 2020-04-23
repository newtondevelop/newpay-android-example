package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdateWalletInteract {

    private final WalletRepositoryType walletRepository;

    public UpdateWalletInteract(WalletRepositoryType walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Single<Wallet[]> update(Wallet wallet, String password, String newPassword) {
        return Single.fromCallable(()-> password)
                .flatMapCompletable(a -> walletRepository.updateWallet(wallet.address, password, newPassword))
                .andThen(walletRepository.fetchWallets())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
