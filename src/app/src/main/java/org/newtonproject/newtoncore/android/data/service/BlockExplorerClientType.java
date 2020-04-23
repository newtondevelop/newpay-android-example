package org.newtonproject.newtoncore.android.data.service;

import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;

import io.reactivex.Observable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface BlockExplorerClientType {
	Observable<TransactionResponse> fetchTransactions(String forAddress, int pageNum, boolean forceUpdate);
	Observable<Transaction> findTransaction(String txid);
}
