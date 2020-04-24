package org.newtonproject.newtoncore.android.viewmodels.home;

import android.app.ActivityManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.entity.response.AssetInfo;
import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchWalletsInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.manager.AccountManager;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.router.MyAddressRouter;
import org.newtonproject.newtoncore.android.router.ScanRouter;
import org.newtonproject.newtoncore.android.router.SendRouter;
import org.newtonproject.newtoncore.android.router.TransactionsRouter;
import org.newtonproject.newtoncore.android.viewmodels.BaseViewModel;
import org.newtonproject.newtoncore.android.views.account.BackupActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
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
public class HomeModel extends BaseViewModel {
    private static final String TAG = "HomeModel";
    private static final long GET_BALANCE_INTERVAL = 10;
    private static final long PING_INTERVAL = 10;

    private FetchWalletsInteract fetchWalletsInteract;
    private MyAddressRouter myAddressRouter;
    private SendRouter mSendRouter;
    private GetDefaultWalletBalance getDefaultWalletBalance;
    private TransactionsRouter transactionsRouter;
    private ScanRouter scanRouter;
    private PreferenceRepositoryType preferenceRepositoryType;
    private NetworkRepositoryType networkRepositoryType;
    private CreateTransactionInteract createTransactionInteract;
    private Disposable balanceDisposable;
    private Disposable disposable;
    private Disposable subDispose;
    private Disposable pingDisposable;

    HomeModel(FetchWalletsInteract fetchWalletsInteract,
              MyAddressRouter myAddressRouter,
              SendRouter sendRouter,
              GetDefaultWalletBalance getDefaultWalletBalance,
              TransactionsRouter transactionsRouter,
              ScanRouter scanRouter,
              PreferenceRepositoryType preferenceRepositoryType,
              NetworkRepositoryType networkRepositoryType,
              CreateTransactionInteract createTransactionInteract
    ) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.myAddressRouter = myAddressRouter;
        this.mSendRouter = sendRouter;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.transactionsRouter = transactionsRouter;
        this.scanRouter = scanRouter;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.networkRepositoryType = networkRepositoryType;
        this.createTransactionInteract = createTransactionInteract;
    }

    public void prepare() {
        wallets.postValue(AccountManager.getInstance().getWallets());
    }

    @Override
    public void onCleared() {
        disposableSelf(disposable);
        disposableSelf(balanceDisposable);
        disposableSelf(pingDisposable);
        super.onCleared();
    }


    public LiveData<Wallet[]> wallets() {
        return wallets;
    }

    private MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();

    public LiveData<Map<String, String>> currentWalletBalance() {
        return currentWalletBalance;
    }

    private MutableLiveData<Map<String, String>> currentWalletBalance = new MutableLiveData<>();

    public LiveData<String> cacheBalance() {
        return cacheBalance;
    }

    private MutableLiveData<String> cacheBalance = new MutableLiveData<>();


    private MutableLiveData<AssetInfo> onAssetInfo = new MutableLiveData<>();

    public LiveData<AssetInfo> onAssertInfo() {
        return onAssetInfo;
    }

    private MutableLiveData<HashMap<String, String>> onPublicKeyAndNewId = new MutableLiveData<>();

    private MutableLiveData<String> onTotalCacheBalance = new MutableLiveData<>();

    public LiveData<String> onTotalCacheBalance() {
        return onTotalCacheBalance;
    }
    private MutableLiveData<Boolean> isNeedBackUpWallet = new MutableLiveData<>();
    public LiveData<Boolean> isNeedBackUpWallet() { return isNeedBackUpWallet; }

    private MutableLiveData<Boolean> onProgress = new MutableLiveData<>();
    public LiveData<Boolean> onProgress() { return onProgress; }

    public void getBalance(Wallet wallet, boolean isShowRate) {
        if(balanceDisposable != null) {
            balanceDisposable.dispose();
        }
        if (balanceDisposable == null || balanceDisposable.isDisposed()) {
            balanceDisposable = Observable
                    .interval(0, GET_BALANCE_INTERVAL, TimeUnit.SECONDS)
                    .doOnNext(l -> {
                        if (balanceDisposable != null && !balanceDisposable.isDisposed()) {
                            subDispose = getDefaultWalletBalance.get(wallet)
                                    .subscribe(currentWalletBalance::postValue, t -> {
                                    });
                        } else {
                            disposableSelf(subDispose);
                        }
                    })
                    .subscribe();
        }
    }

    public void getBalance(Wallet wallet) {
        getBalance(wallet, false);
    }


    public void setDefaultWallet(Wallet wallet) {
        preferenceRepositoryType.setCurrentWalletAddress(wallet.address);
    }

    public void getCacheBalance(Wallet wallet) {
        String cacheBalanceStr = preferenceRepositoryType.getCacheBalance(wallet.address);
        cacheBalance.postValue(cacheBalanceStr);
    }

    public void openWallet(Context context) {
        transactionsRouter.open(context, false);
    }

    public void signMessageWithKey(String access_key, String signNonceMessage) {
        progress.postValue(true);
        Disposable subscribe = createTransactionInteract
                .signMessageWithKey(access_key, signNonceMessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            progress.postValue(false);
//                            onSignNonceMessage.postValue(next);
                        },
                        error -> {
                            progress.postValue(false);
                            onCommonError.postValue(error);
                        }
                );

    }

    public String getWalletName(String address) {
        return preferenceRepositoryType.getWalletName(address);
    }

    /**
     * auto check net work
     */
    public void checkPing(Context context) {
        if (isRunningForeground(context)) {
            if (preferenceRepositoryType.isNeedPing()) {
                startPing();
            }
        } else {
            preferenceRepositoryType.setIsNeedPing(true);
        }
    }

    public void checkNetWork() {
        startPing();
    }

    private void startPing() {
        if (pingDisposable == null || pingDisposable.isDisposed()) {
            pingDisposable = Single.fromCallable(() -> {
                networkRepositoryType.autoChoiceNetwork();
                preferenceRepositoryType.setIsNeedPing(false);
                return true;
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }


    private boolean isRunningForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && processInfo.processName.equals(context.getApplicationInfo().processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * open receive activity
     *
     * @param context
     * @param wallet
     */
    public void openReceive(Context context, Wallet wallet) {
        myAddressRouter.open(context, wallet);
    }

    public void openSend(Context context) {
        mSendRouter.open(context);
    }

    public void openSendWithAddress(Context context, String address, String amount) {
        mSendRouter.openWithAddress(context, address, amount);
    }

    public void openScan(Fragment fragment) {
        scanRouter.openFromFragment(fragment);
    }

    public void signMessageAndGetPublicKey(String currentAddress, String password, String sigmessage) {
        onProgress.postValue(true);
        Disposable subscribe = createTransactionInteract
                .signMessageAndGetPublicKey(currentAddress, password, sigmessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        next -> {
                            onProgress.postValue(false);
                            onPublicKeyAndNewId.postValue(next);
                        },
                        error -> {
                            onProgress.postValue(false);
                            onCommonError.postValue(error);
                        }
                );
    }

    public void setTotalBalanceCache(Wallet currentWallet, String mTotalBalance) {
        preferenceRepositoryType.setTotalBalance(currentWallet.address, mTotalBalance);
    }

    public void getTotalCahceBalance(Wallet currentWallet) {
        onTotalCacheBalance.postValue(preferenceRepositoryType.getTotalBalance(currentWallet.address));
    }

    public void checkHasBackUpWallet() {
        isNeedBackUpWallet.postValue(preferenceRepositoryType.isNeedBackup());
    }

    public void openBackUp(Context mContext) {
        Intent intent = new Intent(mContext, BackupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }

}
