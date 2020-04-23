package org.newtonproject.newtoncore.android.data.cache;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.NewQueryResponse;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.LifeCache;
import io.rx_cache2.ProviderKey;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-05-14--15:27
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */

public interface CacheProvider {

    @ProviderKey("transactionDetail")
    @LifeCache(duration = C.CACHE_TIME_LONG, timeUnit = TimeUnit.SECONDS)
    Observable<NewQueryResponse> findTransaction(Observable<NewQueryResponse> findTransaction, DynamicKey key, EvictDynamicKey evictProvider);

    @ProviderKey("fetchTransactions")
    @LifeCache(duration = C.CACHE_TIME_LONG, timeUnit = TimeUnit.SECONDS)
    Observable<TransactionResponse> fetchTransactions(Observable<TransactionResponse> fetchTransactions, DynamicKeyGroup keyGroup, EvictDynamicKeyGroup evictProvider);

}
