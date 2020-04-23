package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.data.entity.response.BaseResponse;
import org.newtonproject.newtoncore.android.data.entity.response.Faucet;
import org.newtonproject.newtoncore.android.data.entity.response.JsUrl;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/12/29--7:17 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class GuideViewModel extends BaseViewModel {

    private PreferenceRepositoryType mPreference;
    public GuideViewModel(PreferenceRepositoryType preferenceRepositoryType) {
        mPreference = preferenceRepositoryType;
    }

    // get default wallet
    private MutableLiveData<Wallet> onCurrentWallet = new MutableLiveData<>();
    public LiveData<Wallet> onCurrentWallet() {
        return onCurrentWallet;
    }
    private MutableLiveData<Throwable> onCurrentWalletError = new MutableLiveData<>();
    public LiveData<Throwable> onCurrentWalletError() {
        return onCurrentWalletError;
    }

    // get faucet
    private MutableLiveData<Faucet> onFaucet = new MutableLiveData<>();
    public LiveData<Faucet> onFaucet() {
        return onFaucet;
    }
    private MutableLiveData<Throwable> onFaucetError = new MutableLiveData<>();
    public LiveData<Throwable> onFaucetError() {
        return onFaucetError;
    }

    // verify captcha
        // get js url
    private MutableLiveData<JsUrl> onJsUrl = new MutableLiveData<>();
    public LiveData<JsUrl> onJsUrl() {
        return onJsUrl;
    }
    private MutableLiveData<Throwable> onJsUrlError = new MutableLiveData<>();
    public LiveData<Throwable> onJsUrlError() {
        return onJsUrlError;
    }
        // verify captcha
    private MutableLiveData<BaseResponse> onBaseResponse = new MutableLiveData<>();
    public LiveData<BaseResponse> onBaseResponse() {
        return onBaseResponse;
    }
    private MutableLiveData<Throwable> onBaseResponseError = new MutableLiveData<>();
    public LiveData<Throwable> onBaseResponseError() {
        return onBaseResponseError;
    }

}
