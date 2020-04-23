package org.newtonproject.newtoncore.android.data.repository;

import android.text.format.DateUtils;

import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;

import java.util.Map;

import io.reactivex.Single;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
// TODO: Add pagination.
public class TransactionInMemorySource implements TransactionLocalSource {

	private static final long MAX_TIME_OUT = DateUtils.MINUTE_IN_MILLIS;
	private static final String TAG = TransactionInMemorySource.class.getSimpleName();
	private final Map<String, CacheUnit> cache = new java.util.concurrent.ConcurrentHashMap<>();

	@Override
	public Single<TransactionResponse> fetchTransactions(Wallet wallet) {
		return Single.fromCallable(() -> {
			CacheUnit unit = cache.get(wallet.address);
			Transaction[] transactions = new Transaction[0];
			if (unit != null) {
				if (System.currentTimeMillis() - unit.create > MAX_TIME_OUT) {
					cache.remove(wallet.address);
				} else {
					transactions = unit.transactions;
				}
			}
			TransactionResponse res = new TransactionResponse();
			res.docs = transactions;
			res.total = transactions.length;
			return res;
		});
	}

	@Override
	public void putTransactions(Wallet wallet, Transaction[] transactions) {
		cache.put(wallet.address, new CacheUnit(wallet.address, System.currentTimeMillis(), transactions));
	}

    @Override
    public void clear(Wallet wallet) {
        cache.clear();
    }

	@Override
	public Single<Transaction> fetchTransaction(Wallet wallet, String hash) {
		return Single.fromCallable(() -> {
			CacheUnit unit = cache.get(wallet.address);
			Transaction transaction = null;
			if (unit != null) {
				if (System.currentTimeMillis() - unit.create > MAX_TIME_OUT) {
					cache.remove(wallet.address);
				} else {
					Transaction[] transactions = unit.transactions;
					if(transactions != null && transactions.length > 0) {
						for(Transaction t : transactions) {
							if(t.hash.equals(hash)) {
								transaction = t;
							}
						}
					}
				}
			}
			return transaction;
		});
	}

	private static class CacheUnit {
		final String accountAddress;
		final long create;
		final Transaction[] transactions;

		private CacheUnit(String accountAddress, long create, Transaction[] transactions) {
			this.accountAddress = accountAddress;
			this.create = create;
			this.transactions = transactions;
		}
	}
}
