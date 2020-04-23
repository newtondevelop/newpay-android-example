package org.newtonproject.newtoncore.android.data.service;

import org.newtonproject.newtoncore.android.data.entity.common.TokenInfo;

import io.reactivex.Observable;

public interface TokenExplorerClientType {
    Observable<TokenInfo[]> fetch(String walletAddress);
}
