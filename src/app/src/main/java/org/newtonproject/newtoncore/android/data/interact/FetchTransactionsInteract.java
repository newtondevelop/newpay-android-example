package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.TransactionRepositoryType;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class FetchTransactionsInteract {

    private final TransactionRepositoryType transactionRepository;

    public FetchTransactionsInteract(TransactionRepositoryType transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Observable<TransactionResponse> fetch(Wallet wallet, int pageNum, boolean forceUpdate) {
        return transactionRepository
                .fetchTransaction(wallet, pageNum, forceUpdate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Transaction> find(Wallet wallet, String hash) {
        return transactionRepository
                .findTransaction(wallet, hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
