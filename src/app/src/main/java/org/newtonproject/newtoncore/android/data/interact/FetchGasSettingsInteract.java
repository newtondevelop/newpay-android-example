package org.newtonproject.newtoncore.android.data.interact;


import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.data.repository.WalletRepositoryType;
import org.newtonproject.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;

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
public class FetchGasSettingsInteract {
    private final WalletRepositoryType repository;

    public FetchGasSettingsInteract(WalletRepositoryType repository) {
        this.repository = repository;
    }

    public Single<GasSettings> fetch(Transaction transaction) {
        return Single.fromCallable(()-> {
            Single<BigInteger> gasPrice = repository.getGasPrice();
            Single<BigInteger> bigIntegerSingle = repository.estimateGas(transaction);
            BigInteger price = gasPrice.blockingGet();
            BigInteger limit = bigIntegerSingle.blockingGet();
            if(limit.compareTo(BigInteger.valueOf(21000L)) != 0){
                limit = limit.multiply(BigInteger.valueOf(12L)).divide(BigInteger.valueOf(10L));
            }
            return new GasSettings(price, limit);
        } ).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }
}
