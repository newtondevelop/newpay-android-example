package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.TokenInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface TokenLocalSource {
    Completable put(NetworkInfo networkInfo, Wallet wallet, TokenInfo tokenInfo);
    Single<TokenInfo[]> fetch(NetworkInfo networkInfo, Wallet wallet);
}
