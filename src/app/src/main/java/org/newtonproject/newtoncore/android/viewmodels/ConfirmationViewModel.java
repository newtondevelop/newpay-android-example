package org.newtonproject.newtoncore.android.viewmodels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.newtonproject.newtoncore.android.data.entity.common.GasSettings;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.interact.CreateTransactionInteract;
import org.newtonproject.newtoncore.android.data.interact.FetchGasSettingsInteract;
import org.newtonproject.newtoncore.android.data.interact.FindDefaultWalletInteract;
import org.newtonproject.newtoncore.android.data.repository.TokenRepository;
import org.newtonproject.newtoncore.android.router.GasSettingsRouter;
import org.newtonproject.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ConfirmationViewModel extends BaseViewModel {
    private final MutableLiveData<String> newTransaction = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<GasSettings> gasSettings = new MutableLiveData<>();

    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;

    private final GasSettingsRouter gasSettingsRouter;

    boolean confirmationForTokenTransfer = false;

    public ConfirmationViewModel(FindDefaultWalletInteract findDefaultWalletInteract,
                                 FetchGasSettingsInteract fetchGasSettingsInteract,
                                 CreateTransactionInteract createTransactionInteract,
                                 GasSettingsRouter gasSettingsRouter) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.gasSettingsRouter = gasSettingsRouter;
    }

    public void createTransaction(String from, String to, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, String password) {
        progress.postValue(true);
        disposable = createTransactionInteract
                .create(new Wallet(from), to, amount, gasPrice, gasLimit, null, password)
                .subscribe(this::onCreateTransaction, this::onError);
    }

    public void createTokenTransfer(String from, String to, String contractAddress, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, String password) {
        progress.postValue(true);
        final byte[] data = TokenRepository.createTokenTransferData(to, amount);
        disposable = createTransactionInteract
                .create(new Wallet(from), contractAddress, BigInteger.valueOf(0), gasPrice, gasLimit, data, password)
                .subscribe(this::onCreateTransaction, this::onError);
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<GasSettings> gasSettings() {
        return gasSettings;
    }

    public LiveData<String> sendTransaction() { return newTransaction; }

    public void prepare(boolean confirmationForTokenTransfer) {
        this.confirmationForTokenTransfer = confirmationForTokenTransfer;
        disposable = findDefaultWalletInteract
                .find()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    private void onCreateTransaction(String transaction) {
        progress.postValue(false);
        newTransaction.postValue(transaction);
    }

    private void onDefaultWallet(Wallet wallet) {
        defaultWallet.setValue(wallet);
    }

    public void getGasSettings(Transaction transaction) {
        progress.postValue(true);
        disposable = fetchGasSettingsInteract.fetch(transaction)
                .subscribe(this::onGasSettings, this::onError);
    }

    public void onGasSettings(GasSettings gas) {
        progress.postValue(false);
        gasSettings.postValue(gas);
    }

    public void openGasSettings(Activity context) {
        gasSettingsRouter.open(context, gasSettings.getValue());
    }
}
