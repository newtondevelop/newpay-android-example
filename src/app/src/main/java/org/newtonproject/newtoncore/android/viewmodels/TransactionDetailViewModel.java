package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Transaction;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.FetchTransactionsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.router.ExternalBrowserRouter;
import org.newtonproject.newtoncore.android.views.intro.WebViewActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionDetailViewModel extends BaseViewModel {

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final ExternalBrowserRouter externalBrowserRouter;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Transaction> currentTransaction = new MutableLiveData<>();
    private final Disposable networkDisposable;
    private Disposable transactionDisposable;

    TransactionDetailViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExternalBrowserRouter externalBrowserRouter,
            FetchTransactionsInteract fetchTransactionsInteract) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.externalBrowserRouter = externalBrowserRouter;
        this.fetchTransactionsInteract = fetchTransactionsInteract;


        networkDisposable = findDefaultNetworkInteract
                .find()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(defaultNetwork::postValue);
        disposable = findDefaultWalletInteract
                .find()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(defaultWallet::postValue);
    }


    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public void showMoreDetails(Context context, Transaction transaction) {
        NetworkInfo networkInfo = defaultNetwork.getValue();
        if (networkInfo != null && !TextUtils.isEmpty(networkInfo.explorerUrl)) {
            Uri uri = Uri.parse(networkInfo.explorerUrl)
                    .buildUpon()
                    .appendEncodedPath("tx")
                    .appendEncodedPath(transaction.hash)
                    .build();
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(C.EXTRA_TITLE, C.NewExplorer);
            intent.putExtra(C.EXTRA_URL, uri.toString());
            context.startActivity(intent);
            //externalBrowserRouter.open(context, uri);
        }
    }

    public void shareTransactionDetail(Context context, Transaction transaction) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject_transaction_detail));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, buildEtherscanUri(transaction).toString());
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_via)));
    }

    private Uri buildEtherscanUri(Transaction transaction) {
        NetworkInfo networkInfo = defaultNetwork.getValue();
        if (networkInfo != null && !TextUtils.isEmpty(networkInfo.explorerUrl) && transaction != null) {
            return Uri.parse(networkInfo.explorerUrl)
                    .buildUpon()
                    .appendEncodedPath("tx")
                    .appendEncodedPath(transaction.hash)
                    .build();
        }
        return null;
    }

    public void findTransaction(Wallet wallet, String hash) {
        progress.postValue(true);
        transactionDisposable = fetchTransactionsInteract
                .find(wallet, hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next->{
                            progress.postValue(false);
                            currentTransaction.postValue(next);
                        },
                        error->{
                            progress.postValue(false);
                            onCommonError.postValue(error);
                        });
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Transaction> currentTransaction() { return currentTransaction; }
}
