package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;

import io.reactivex.disposables.Disposable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SplashViewModel extends BaseViewModel {

    private static final String TAG = "SplashViewModel";
    private FetchWalletsInteract fetchWalletsInteract;
    private PreferenceRepositoryType preferenceRepositoryType;
    private Disposable disposable;
    private Gson gson = new Gson();
    SplashViewModel(FetchWalletsInteract fetchWalletsInteract, PreferenceRepositoryType preferenceRepositoryType) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    public void prepare() {
        fetchWallet();
    }

    public void fetchWallet() {
        disposable = fetchWalletsInteract
                .fetch()
                .subscribe(list  -> {
                    wallets.postValue(list);
                }, this::onError);
    }

    private MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();

    public LiveData<Wallet[]> wallets() {
        return wallets;
    }

    @Override
    public void onCleared() {
        disposableSelf(disposable);
        super.onCleared();
    }
}
