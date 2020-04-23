package org.newtonproject.newtoncore.android.data.interact;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.data.repository.NetworkRepositoryType;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class FindDefaultNetworkInteract {

    private final NetworkRepositoryType ethereumNetworkRepository;

    public FindDefaultNetworkInteract(NetworkRepositoryType ethereumNetworkRepository) {
        this.ethereumNetworkRepository = ethereumNetworkRepository;
    }

    public Single<NetworkInfo> find() {
        return Single.just(ethereumNetworkRepository.getDefaultNetwork())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
