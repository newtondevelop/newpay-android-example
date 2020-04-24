package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.entity.response.DEXInfo;
import org.newtonproject.newtoncore.android.data.entity.response.MarketToken;
import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchGasSettingsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.interact.GetDefaultWalletBalance;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
import org.newtonproject.newtoncore.android.router.GasSettingsRouter;
import org.newtonproject.newtoncore.android.router.ScanRouter;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.views.intro.WebViewActivity;
import org.newtonproject.web3j.protocol.core.methods.request.Transaction;
import org.newtonproject.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Map;

import io.reactivex.disposables.Disposable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SendViewModel extends BaseViewModel {

    private static String TAG = SendViewModel.class.getSimpleName();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<String> newTransaction = new MutableLiveData<>();
    private final MutableLiveData<GasSettings> gasSettings = new MutableLiveData<>();
    private final MutableLiveData<Throwable> onGasError = new MutableLiveData<>();
    private final MutableLiveData<Throwable> onDefaultWalletError = new MutableLiveData<>();
    private final MutableLiveData<Throwable> onCreateTransactionError = new MutableLiveData<>();
    private final MutableLiveData<Throwable> onBalanceError = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> currentWalletBalance = new MutableLiveData<>();
    private final MutableLiveData<String> onCacheBalance = new MutableLiveData<>();

    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;
    private final PreferenceRepositoryType preferenceRepositoryType;
    private final GetDefaultWalletBalance getDefaultWalletBalance;

    private final ScanRouter scanRouter;
    private final GasSettingsRouter gasSettingsRouter;

    private Disposable balanceDisposable;

    SendViewModel(ScanRouter scanRouter, GasSettingsRouter gasSettingsRouter,
                  FindDefaultWalletInteract findDefaultWalletInteract,
                  FetchGasSettingsInteract fetchGasSettingsInteract,
                  CreateTransactionInteract createTransactionInteract,
                  PreferenceRepositoryType preferenceRepositoryType,
                  GetDefaultWalletBalance getDefaultWalletBalance) {
        this.scanRouter = scanRouter;
        this.gasSettingsRouter = gasSettingsRouter;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Map<String, String>> currentWalletBalance() {
        return currentWalletBalance;
    }

    public LiveData<GasSettings> gasSettings() {
        return gasSettings;
    }

    public LiveData<Throwable> onGasError() {
        return onGasError;
    }

    public LiveData<Throwable> onCreateTransactionError() {
        return onCreateTransactionError;
    }
    public LiveData<Throwable> onBalanceError() { return onBalanceError; }

    public LiveData<String> sendTransaction() {
        return newTransaction;
    }

    public LiveData<String> onCacheBalance() { return onCacheBalance; }

    public LiveData<Throwable> onDefaultWalletError() {
        return onDefaultWalletError;
    }


    public void openScan(Context context) {
        scanRouter.open(context);
    }

    public void createTransaction(String from, String to, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, String password, byte[] data) {
        progress.postValue(true);
        disposable = createTransactionInteract
                .create(new Wallet(from), to, amount, gasPrice, gasLimit, data, password)
                .subscribe(next->{
                    progress.postValue(false);
                    newTransaction.postValue(next);
                }, error->{
                    progress.postValue(false);
                    onCreateTransactionError.postValue(error);
                });
    }

    public void prepare() {
        progress.postValue(true);
        disposable = findDefaultWalletInteract
                .find()
                .subscribe(next->{
                    progress.postValue(false);
                    defaultWallet.postValue(next);
                }, error-> {
                    progress.postValue(false);
                    onDefaultWalletError.postValue(error);
                    onError(error);
                });
    }

    public void getGasSettings(String from, String to) {
        progress.postValue(true);
        disposable = fetchGasSettingsInteract
                .fetch(Transaction.createEthCallTransaction(NewAddressUtils.newAddress2HexAddress(from), NewAddressUtils.newAddress2HexAddress(to), null))
                .subscribe(next->{
                    progress.postValue(false);
                    gasSettings.postValue(next);
                }, error->{
                    progress.postValue(false);
                    Log.e(TAG, "Gas error");
                    onGasError.postValue(error);
                });
    }

    public void getGasSettings(String from, String to,BigInteger value, String data) {
        if(TextUtils.isEmpty(data)) {
            getGasSettings(from, to);
        }else{
            progress.postValue(true);
            disposable = fetchGasSettingsInteract.fetch(Transaction.createFunctionCallTransaction(from, BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO, to, value, Numeric.toHexString(data.getBytes())))
                    .subscribe(next->{
                        progress.postValue(false);
                        gasSettings.postValue(next);
                    }, error->{
                        progress.postValue(false);
                        Log.e(TAG, "Gas error");
                        onGasError.postValue(error);
                    });
        }
    }

    public void getCacheBalance(Wallet wallet) {
        onCacheBalance.postValue(preferenceRepositoryType.getCacheBalance(wallet.address));
    }

    private MutableLiveData<DEXInfo> onDexInfo = new MutableLiveData<>();
    public LiveData<DEXInfo> onDexInfo() { return onDexInfo; }

    public void openWebView(Context mContext, String title, String url) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(C.EXTRA_URL, url);
        intent.putExtra(C.EXTRA_TITLE, title);
        mContext.startActivity(intent);
    }

    // on market token
    private MutableLiveData<MarketToken> onMarketToken = new MutableLiveData<>();
    public LiveData<MarketToken> onMarketToken() { return onMarketToken; }
}
