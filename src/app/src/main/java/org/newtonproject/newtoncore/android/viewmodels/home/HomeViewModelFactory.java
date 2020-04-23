package org.newtonproject.newtoncore.android.viewmodels.home;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.router.MyAddressRouter;
import org.newtonproject.newtoncore.android.router.ScanRouter;
import org.newtonproject.newtoncore.android.router.SendRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final FetchWalletsInteract fetchWalletsInteract;
    private final MyAddressRouter myAddressRouter;
    private final SendRouter sendRouter;
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final TransactionsRouter transactionsRouter;
    private final ScanRouter scanRouter;
    private final PreferenceRepositoryType preferenceRepositoryType;
    private final NetworkRepositoryType networkRepositoryType;
    private CreateTransactionInteract createTransactionInteract;

    public HomeViewModelFactory(FetchWalletsInteract fetchWalletsInteract,
                                MyAddressRouter myAddressRouter,
                                SendRouter sendRouter,
                                GetDefaultWalletBalance getDefaultWalletBalance,
                                TransactionsRouter transactionsRouter,
                                ScanRouter scanRouter,
                                PreferenceRepositoryType preferenceRepositoryType,
                                NetworkRepositoryType networkRepositoryType,
                                CreateTransactionInteract createTransactionInteract) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.myAddressRouter = myAddressRouter;
        this.sendRouter = sendRouter;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.transactionsRouter = transactionsRouter;
        this.scanRouter = scanRouter;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.networkRepositoryType = networkRepositoryType;
        this.createTransactionInteract = createTransactionInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeModel(fetchWalletsInteract,
                myAddressRouter,
                sendRouter,
                getDefaultWalletBalance,
                transactionsRouter,
                scanRouter,
                preferenceRepositoryType,
                networkRepositoryType,
                createTransactionInteract);
    }
}
