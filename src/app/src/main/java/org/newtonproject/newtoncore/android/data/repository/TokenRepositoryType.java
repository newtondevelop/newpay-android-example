package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Token;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import io.reactivex.Completable;
import io.reactivex.Observable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface TokenRepositoryType {

    Observable<Token[]> fetch(String walletAddress);

    Completable addToken(Wallet wallet, String address, String symbol, int decimals);
}
