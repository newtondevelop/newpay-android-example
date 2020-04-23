package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.TransactionResponse;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.FetchTransactionsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.router.ExternalBrowserRouter;
import org.newtonproject.newtoncore.android.router.TransactionDetailRouter;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionsViewModel extends BaseViewModel {
    private static final String TAG = TransactionsViewModel.class.getSimpleName();

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    private final TransactionDetailRouter transactionDetailRouter;
    private final ExternalBrowserRouter externalBrowserRouter;
    private final PreferenceRepositoryType preferenceRepositoryType;

    TransactionsViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            GetDefaultWalletBalance getDefaultWalletBalance,
            TransactionDetailRouter transactionDetailRouter,
            ExternalBrowserRouter externalBrowserRouter,
            PreferenceRepositoryType preferenceRepositoryType) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
        this.transactionDetailRouter = transactionDetailRouter;
        this.externalBrowserRouter = externalBrowserRouter;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    @Override
    public void onCleared() {
        disposableSelf(disposable);
        super.onCleared();
    }

    // get default network
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    // get default wallet
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    // get default balance
    private final MutableLiveData<Map<String, String>> defaultWalletBalance = new MutableLiveData<>();
    public LiveData<Map<String, String>> defaultWalletBalance() {
        return defaultWalletBalance;
    }

    // get cache balance
    public LiveData<String> cacheBalance() { return cacheBalance; }
    private final MutableLiveData<String> cacheBalance = new MutableLiveData<>();

    // first get transaction form network
    private final MutableLiveData<TransactionResponse> transactions = new MutableLiveData<>();
    public LiveData<TransactionResponse> transactions() {
        return transactions;
    }

    // transaction error
    private MutableLiveData<Throwable> onTransactionError = new MutableLiveData<>();
    public LiveData<Throwable> onTransactionError() { return onTransactionError; }

    public void prepare() {
        getDefaultWallet();
    }

    public void fetchTransactions(Wallet wallet, int pageId, boolean forceUpdate) {
        progress.postValue(true);
        disposable = fetchTransactionsInteract
                .fetch(wallet, pageId, forceUpdate)
                .subscribe(
                        next->{
                            progress.postValue(false);
                            transactions.postValue(next);
                        },
                        error->{
                            progress.postValue(false);
                            onTransactionError.postValue(error);
                        }
                );

    }

    public void getBalance(Wallet wallet) {
        Disposable subscribe = getDefaultWalletBalance
                .get(wallet)
                .subscribe(defaultWalletBalance::postValue, t -> {
                });
    }

    public void getCacheBalance(Wallet wallet) {
        cacheBalance.postValue(preferenceRepositoryType.getCacheBalance(wallet.address));
    }

    public void getDefaultWallet() {
        Disposable subscribe = findDefaultWalletInteract.find().subscribe((wallet, throwable) -> defaultWallet.postValue(wallet));
    }

    public void showDetails(Context context, Transaction transaction) {
        transactionDetailRouter.open(context, transaction);
    }

}
