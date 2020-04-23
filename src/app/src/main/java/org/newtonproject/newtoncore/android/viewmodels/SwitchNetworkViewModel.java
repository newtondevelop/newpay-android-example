package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;
import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/10/20--10:52 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SwitchNetworkViewModel extends BaseViewModel {

    private final NetworkRepositoryType networkRepositoryType;
    private final PreferenceRepositoryType preferenceRepositoryType;

    SwitchNetworkViewModel(NetworkRepositoryType networkRepositoryType,
                           PreferenceRepositoryType preferenceRepositoryType) {
        this.networkRepositoryType = networkRepositoryType;
        this.preferenceRepositoryType = preferenceRepositoryType;
    }

    private MutableLiveData<NetworkInfo[]> onNetworkLists = new MutableLiveData<>();
    public LiveData<NetworkInfo[]> onNetworkLists() { return onNetworkLists; }

    public void getNetworkList() {
        onNetworkLists.postValue(networkRepositoryType
                .getAvailableNetworkList());
        Single.fromCallable(() -> {
            networkRepositoryType.autoChoiceNetwork();
            return true;
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void setDefaultNetwork(NetworkInfo network) {
        networkRepositoryType.setDefaultNetworkInfo(network);
        preferenceRepositoryType.setDefaultNetwork(network.name);
    }

    public String getDefaultNetwork() {
        return preferenceRepositoryType.getDefaultNetwork();
    }

    public void updateNetworkConfig() {
        networkRepositoryType.updateNetworkConfig();
    }
}
