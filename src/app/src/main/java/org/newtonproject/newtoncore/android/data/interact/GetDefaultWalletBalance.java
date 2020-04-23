package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.newtoncore.android.utils.BalanceUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.newtonproject.newtoncore.android.utils.BalanceUtils.weiToNEW;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class GetDefaultWalletBalance {

    private static final String TAG = "GetBalance";
    private final WalletRepositoryType walletRepository;
    private final NetworkRepositoryType ethereumNetworkRepository;
    private final PreferenceRepositoryType preferenceRepositoryType;

    public GetDefaultWalletBalance(
            WalletRepositoryType walletRepository,
            NetworkRepositoryType ethereumNetworkRepository,
            PreferenceRepositoryType preferenceRepositoryType) {
        this.walletRepository = walletRepository;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    public Single<Map<String, String>> get(Wallet wallet) {
        return walletRepository.balanceInWei(wallet)
                .flatMap(isaacBalance -> {
                    Map<String, String> balances = new HashMap<>();
                    preferenceRepositoryType.setCacheBalance(wallet.address, BalanceUtils.subunitToBase(isaacBalance, C.ETHER_DECIMALS).toString());
                    String balance = weiToNEW(isaacBalance);
                    balances.put(ethereumNetworkRepository.getDefaultNetwork().symbol, balance);
                    return Single.just(balances);
                })
//                .flatMap(balances -> ethereumNetworkRepository
//                        .getTicker()
//                        .observeOn(Schedulers.io())
//                        .flatMap(ticker -> {
//                            String ethBallance = balances.get(ethereumNetworkRepository.getDefaultNetwork().symbol);
//                            balances.put(USD_SYMBOL, BalanceUtils.ethToUsd(ticker.price, ethBallance));
//                            return Single.just(balances);
//                        })
//                        .onErrorResumeNext(throwable -> Single.just(balances)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
