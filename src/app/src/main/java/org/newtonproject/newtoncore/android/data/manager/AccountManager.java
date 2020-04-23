package org.newtonproject.newtoncore.android.data.manager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.newtonproject.newtoncore.android.App;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/5/13--11:06 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AccountManager {

    private AccountManager() {}
    private static AccountManager mInstance;
    private static Wallet[] mWallets;
    private static Wallet mDefaultWallet;
    // init instance
    public static AccountManager getInstance() {
        if (mInstance == null) {
            synchronized (AccountManager.class) {
                if (mInstance == null) {
                    mInstance = new AccountManager();
                }
            }
        }
        return mInstance;
    }

    // init observable account;
    private MutableLiveData<Wallet[]> observableWallets = new MutableLiveData<>();
    public LiveData<Wallet[]> observableWallets() { return observableWallets; }

    private MutableLiveData<Wallet> observableWallet = new MutableLiveData<>();
    public LiveData<Wallet> observableWallet() { return observableWallet; }

    public void initAccounts(Wallet[] wallets) {
        if(wallets == null || wallets.length == 0) {
            throw new IllegalArgumentException("wallets can not be null or wallet's length == 0");
        }
        mWallets = wallets;
        observableWallets.postValue(mWallets);
        mDefaultWallet = wallets[0];
        observableWallet.postValue(mDefaultWallet);
    }

    public void initAccount(Wallet wallet) {
        if(wallet == null) {
            throw new IllegalArgumentException("wallet can not be null.");
        }
        mDefaultWallet = wallet;
        observableWallet.postValue(mDefaultWallet);
    }

    public Wallet[] getWallets() {
        if(mWallets == null || mWallets.length == 0) {
            Wallet defaultWallet = getDefaultWallet();
            mWallets = new Wallet[]{defaultWallet};
        }
        return mWallets;
    }

    public Wallet getDefaultWallet() {
        if(mDefaultWallet == null) {
            SharedPreferenceRepository preferenceRepository = new SharedPreferenceRepository(App.getAppContext());
            String currentWalletAddress = preferenceRepository.getCurrentWalletAddress();
            mDefaultWallet = new Wallet(NewAddressUtils.hexAddress2NewAddress(currentWalletAddress));
        }
        return mDefaultWallet;
    }

    public void updateWallets(Wallet[] wallets) {
        // todo: change account...
    }

    public void clear() {
        mWallets = null;
        mDefaultWallet = null;
    }
}
