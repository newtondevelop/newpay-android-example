package org.newtonproject.newtoncore.android.data.repository;

import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public interface NetworkRepositoryType {

	NetworkInfo getDefaultNetwork();

	void setDefaultNetworkInfo(NetworkInfo networkInfo);

	NetworkInfo[] getAvailableNetworkList();

	void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged);

	void autoChoiceNetwork();

    void updateNetworkConfig();
}
