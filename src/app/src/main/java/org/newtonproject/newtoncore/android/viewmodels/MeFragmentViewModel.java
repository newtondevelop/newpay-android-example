package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import org.newtonproject.newtoncore.android.data.entity.response.Faucet;
import org.newtonproject.newtoncore.android.data.entity.response.ProfileInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.router.FriendsRouter;
import org.newtonproject.newtoncore.android.router.SettingsRouter;
import org.newtonproject.newtoncore.android.router.WalletsListRouter;
import org.newtonproject.newtoncore.android.views.account.BackupActivity;
import org.newtonproject.newtoncore.android.views.contact.AddressBookActivity;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MeFragmentViewModel extends BaseViewModel {

    private WalletsListRouter walletsListRouter;
    private FriendsRouter friendsRouter;
    private SettingsRouter settingsRouter;

    private PreferenceRepositoryType preferenceRepositoryType;
    private CreateTransactionInteract createTransactionInteract;

    private String TAG = "MeFragmentViewModel";

    private MutableLiveData<String> onCurrentAddress = new MutableLiveData<>();
    public LiveData<String> onCurrentAddress() { return onCurrentAddress; }

    // recover newid
    private MutableLiveData<HashMap<String, String>> onPublicKeyAndNewId = new MutableLiveData<>();
    public LiveData<HashMap<String, String>> onPublicKeyAndNewId(){return onPublicKeyAndNewId; }
    // remote profile
    private MutableLiveData<ProfileInfo> onRemoteProfile = new MutableLiveData<>();
    public LiveData<ProfileInfo> onRemoteProfile() { return onRemoteProfile; }
    // on cache balance
    private MutableLiveData<String> onCacheBalance = new MutableLiveData<>();
    public LiveData<String> onCacheBalance() { return onCacheBalance; }
    // on check need backup
    private MutableLiveData<Boolean> isNeedBackup = new MutableLiveData<>();
    public LiveData<Boolean> isNeedBackup() { return isNeedBackup; }
    // on faucet
    private MutableLiveData<Faucet> onFaucet = new MutableLiveData<>();
    public LiveData<Faucet> onFaucet() { return onFaucet; }

    MeFragmentViewModel(WalletsListRouter walletsListRouter,
                        FriendsRouter friendsRouter,
                        SettingsRouter settingsRouter,
                        PreferenceRepositoryType preferenceRepositoryType,
                        CreateTransactionInteract createTransactionInteract) {
        this.walletsListRouter = walletsListRouter;
        this.friendsRouter = friendsRouter;
        this.settingsRouter = settingsRouter;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.createTransactionInteract = createTransactionInteract;
    }

    public void getDefaultWallet() {
        // TODO:针对联系人，通知
        onCurrentAddress.postValue(AccountManager.getInstance().getDefaultWallet().address);
    }

    public void openWalletList(Context context) {
        walletsListRouter.open(context);
    }


    public void openFriend(Context context, Wallet wallet) {
        friendsRouter.open(context, wallet, AddressBookActivity.SOURCE_ME);
    }


    public void openSetting(Context context) {
        settingsRouter.open(context);
    }


    @Override
    public void onCleared() {
        super.onCleared();
    }

    public void signMessageAndGetPublicKey(String currentAddress, String password, String sigmessage) {
        progress.postValue(true);
        Disposable subscribe = createTransactionInteract
                .signMessageAndGetPublicKey(currentAddress, password, sigmessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            progress.postValue(false);
                            onPublicKeyAndNewId.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onCommonError.postValue(error);
                        }
                );
    }

    public void getCacheBalance(String address) {
        onCacheBalance.postValue(preferenceRepositoryType
                .getCacheBalance(address));
    }

    public void checkNeedBackUp() {
        isNeedBackup.postValue(preferenceRepositoryType.isNeedBackup());
    }

    public void openBackUp(Context mContext) {
        Intent intent = new Intent(mContext, BackupActivity.class);
        mContext.startActivity(intent);
    }
}
