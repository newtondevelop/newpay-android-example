package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.interact.FetchTransactionsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.router.ExternalBrowserRouter;
import org.newtonproject.newtoncore.android.router.TransactionDetailRouter;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionsViewModelFactory implements ViewModelProvider.Factory {

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final FetchTransactionsInteract fetchTransactionsInteract;
    private final TransactionDetailRouter transactionDetailRouter;
    private final ExternalBrowserRouter externalBrowserRouter;
    private final PreferenceRepositoryType preferenceRepositoryType;

    public TransactionsViewModelFactory(
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

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TransactionsViewModel(
                findDefaultNetworkInteract,
                findDefaultWalletInteract,
                fetchTransactionsInteract,
                getDefaultWalletBalance,
                transactionDetailRouter,
                externalBrowserRouter,
                preferenceRepositoryType);
    }
}
