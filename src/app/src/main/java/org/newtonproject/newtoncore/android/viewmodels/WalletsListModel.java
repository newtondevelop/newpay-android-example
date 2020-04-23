package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class WalletsListModel extends BaseViewModel{

    private final FetchWalletsInteract fetchWalletsInteract;
    private MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();

    public WalletsListModel(FetchWalletsInteract fetchWalletsInteract) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        fetchWalletsInteract
                .fetch()
                .subscribe(wallets::postValue, this::onError);
    }

    public void onError(Throwable throwable) {
        fetchWalletsInteract
                .fetch()
                .subscribe(wallets::postValue, this::onError);
    }
    
    public LiveData<Wallet[]> wallets() {return wallets; }

    public void fetchWallet() {
        fetchWalletsInteract
                .fetch()
                .subscribe(wallets::postValue, this::onError);
    }
}
