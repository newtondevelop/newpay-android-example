package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.newtonproject.newtoncore.android.data.interact.FetchTokensInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultNetworkInteract;
import org.newtonproject.newtoncore.android.router.AddTokenRouter;
import org.newtonproject.newtoncore.android.router.SendTokenRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TokensViewModelFactory implements ViewModelProvider.Factory {

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FetchTokensInteract fetchTokensInteract;
    private final AddTokenRouter addTokenRouter;
    private final SendTokenRouter sendTokenRouter;
    private final TransactionsRouter transactionsRouter;

    public TokensViewModelFactory(FindDefaultNetworkInteract findDefaultNetworkInteract,
                                  FetchTokensInteract fetchTokensInteract,
                                  AddTokenRouter addTokenRouter,
                                  SendTokenRouter sendTokenRouter,
                                  TransactionsRouter transactionsRouter) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.fetchTokensInteract = fetchTokensInteract;
        this.addTokenRouter = addTokenRouter;
        this.sendTokenRouter = sendTokenRouter;
        this.transactionsRouter = transactionsRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TokensViewModel(
                findDefaultNetworkInteract,
                fetchTokensInteract,
                addTokenRouter,
                sendTokenRouter,
                transactionsRouter);
    }
}
