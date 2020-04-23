package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.repository.TokenRepositoryType;
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
public class AddTokenInteract {
    private final TokenRepositoryType tokenRepository;
    private final WalletRepositoryType walletRepository;

    public AddTokenInteract(
            WalletRepositoryType walletRepository, TokenRepositoryType tokenRepository) {
        this.walletRepository = walletRepository;
        this.tokenRepository = tokenRepository;
    }

    public Completable add(String address, String symbol, int decimals) {
        return walletRepository
                .getDefaultWallet()
                .flatMapCompletable(wallet -> tokenRepository
                        .addToken(wallet, address, symbol, decimals)
                        .observeOn(AndroidSchedulers.mainThread()));
    }
}
