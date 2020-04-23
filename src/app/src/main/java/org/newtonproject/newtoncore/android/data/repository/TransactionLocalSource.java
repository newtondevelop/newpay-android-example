package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import io.reactivex.Single;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface TransactionLocalSource {
	Single<TransactionResponse> fetchTransactions(Wallet wallet);

	void putTransactions(Wallet wallet, Transaction[] transactions);

    void clear(Wallet wallet);

    Single<Transaction> fetchTransaction(Wallet wallet, String hash);
}
