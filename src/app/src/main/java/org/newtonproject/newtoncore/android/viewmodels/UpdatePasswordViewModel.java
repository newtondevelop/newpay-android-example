package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.UpdateWalletInteract;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdatePasswordViewModel extends BaseViewModel {
    private final FetchWalletsInteract fetchWalletsInteract;
    private final UpdateWalletInteract updateWalletInteract;

    private final MutableLiveData<Throwable> onUpdateError = new MutableLiveData<>();
    private final MutableLiveData<Wallet[]> onWallets = new MutableLiveData<>();

    public LiveData<Throwable> onUpdateError() { return onUpdateError; }
    public LiveData<Wallet[]> onWallets() { return onWallets; }

    public UpdatePasswordViewModel(UpdateWalletInteract updateWalletInteract,
                            FetchWalletsInteract fetchWalletsInteract) {
        this.updateWalletInteract = updateWalletInteract;
        this.fetchWalletsInteract = fetchWalletsInteract;
    }

    public void updateWallet(Wallet wallet, String password, String newPassword) {
        progress.postValue(true);
        disposable = updateWalletInteract
                .update(wallet, password, newPassword)
                .subscribe(
                        next->{
                            progress.postValue(false);
                            onWallets.postValue(next);
                        },
                        error->{
                            progress.postValue(false);
                            onUpdateError.postValue(error);
                        }
                );
    }
}
