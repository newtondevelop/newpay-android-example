package org.newtonproject.newtoncore.android.data.repository;

import android.text.TextUtils;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.entity.common.NetworkInfo;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.utils.StringUtil;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import static org.newtonproject.newtoncore.android.C.NEW_SYMBOL;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NetworkRepository implements NetworkRepositoryType {

	private NetworkInfo[] NETWORKS;

	private NetworkInfo[] getNetworks() {
		NetworkInfo[] networkInfos;
		switch (C.CURRENT_NET) {
			case C.NET_TYPE.MAINNET:
				networkInfos = getMainNetWorks();
				break;
			case C.NET_TYPE.BETANET:
			case C.NET_TYPE.TESTNET:
				networkInfos = getTestNetworks();
				break;
			case C.NET_TYPE.DEVNET:
				networkInfos = getDevNetWorks();
				break;
			default:
				networkInfos = getMainNetWorks();
				break;
		}
		return networkInfos;
	}

	private NetworkInfo[] getDevNetWorks() {
		return new NetworkInfo[]{
				new NetworkInfo(StringUtil.getString(R.string.huabei_net), NEW_SYMBOL,
						C.CURRENT_RPC_URL,
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID,
						false),
		};
	}

	private NetworkInfo[] getTestNetworks() {
		return new NetworkInfo[]{
				new NetworkInfo(StringUtil.getString(R.string.west_us_net), NEW_SYMBOL,
						C.RPC2,
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID,
						false),
				new NetworkInfo(StringUtil.getString(R.string.huabei_net), NEW_SYMBOL,
						C.CURRENT_RPC_URL,
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID,
						false),
				new NetworkInfo(StringUtil.getString(R.string.singapore_net), NEW_SYMBOL,
						C.RPC_URL_BETANET,
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID, false),

		};
	}

	private NetworkInfo[] getMainNetWorks() {
		return new NetworkInfo[]{
				new NetworkInfo(StringUtil.getString(R.string.global_net), NEW_SYMBOL,
						"https://global.rpc.mainnet.newtonproject.org",
						C.CURRENT_EXPLORE_URL, C.CURRENT_CHAIN_ID, true),
				new NetworkInfo(StringUtil.getString(R.string.west_us_net), NEW_SYMBOL,
						"https://us.rpc.mainnet.newtonproject.org/",
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID,
						true),
				new NetworkInfo(StringUtil.getString(R.string.huabei_net), NEW_SYMBOL,
						"https://cn.rpc.mainnet.diynova.com/",
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID,
						true),
				new NetworkInfo(StringUtil.getString(R.string.hongkong_net), NEW_SYMBOL,
						"https://hk.rpc.mainnet.diynova.com/",
						C.CURRENT_EXPLORE_URL, C.CURRENT_CHAIN_ID, true),
				new NetworkInfo(StringUtil.getString(R.string.japan_net), NEW_SYMBOL,
						"https://jp.rpc.mainnet.newtonproject.org/",
						C.CURRENT_EXPLORE_URL,
						C.CURRENT_CHAIN_ID,
						true),
				new NetworkInfo(StringUtil.getString(R.string.singapore_net), NEW_SYMBOL,
						"https://sg.rpc.mainnet.newtonproject.org/",
						C.CURRENT_EXPLORE_URL, C.CURRENT_CHAIN_ID, true),
		};
	}

	private final PreferenceRepositoryType preferences;
    private NetworkInfo defaultNetwork;
    private final Set<OnNetworkChangeListener> onNetworkChangedListeners = new HashSet<>();

    public NetworkRepository(PreferenceRepositoryType preferenceRepository) {
		this.preferences = preferenceRepository;
		NETWORKS = getNetworks();
		defaultNetwork = getByName(preferences.getDefaultNetwork());
		if (defaultNetwork == null) {
			preferences.setDefaultNetwork(NETWORKS[0].name);
			defaultNetwork = NETWORKS[0];
		}
	}

	private NetworkInfo getByName(String name) {
		if (!TextUtils.isEmpty(name)) {
			for (NetworkInfo NETWORK : NETWORKS) {
				if (name.equals(NETWORK.name)) {
					return NETWORK;
				}
			}
		}
		return null;
	}

	@Override
	public NetworkInfo getDefaultNetwork() {
		return defaultNetwork;
	}

	@Override
	public void setDefaultNetworkInfo(NetworkInfo networkInfo) {
		defaultNetwork = networkInfo;
		if(defaultNetwork != null) {
			preferences.setDefaultNetwork(defaultNetwork.name);
			Logger.d("ping", "changeTo: " + defaultNetwork.name);
			networkChanged(defaultNetwork);
		}
	}

	@Override
	public NetworkInfo[] getAvailableNetworkList() {
		return NETWORKS;
	}

	@Override
	public void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged) {
        onNetworkChangedListeners.add(onNetworkChanged);
	}

	@Override
	public void autoChoiceNetwork() {
    	HashMap<String, Long> map = new HashMap<>();
		Disposable subscribe = Observable
				.fromArray(NETWORKS)
				.subscribe(request -> {
					long startTime = System.currentTimeMillis();
					Logger.e(request.rpcServerUrl);
					Web3j web3j = Web3jFactory.build(new HttpService(request.rpcServerUrl));
					web3j.netVersion().observable().subscribe(e -> {
						long endTime = System.currentTimeMillis();
						map.put(request.name, endTime - startTime);
						Logger.e("accessed is true");
						request.setAccessed(true);
					}, e -> {
						Logger.e("accessed is false");
						map.put(request.name, Long.MAX_VALUE);
						request.setAccessed(false);
						Logger.e(e.getMessage());
					});
				});
		Set<Map.Entry<String, Long>> entries = map.entrySet();
		Long temp = Long.MAX_VALUE;
		String defaultName = getDefaultNetwork().name;
		for(Map.Entry<String, Long> entry : entries) {
			Long value = entry.getValue();
			if(value.compareTo(temp) < 0) {
				temp = value;
				defaultName = entry.getKey();
			}
		}
		NetworkInfo networkInfo = getByName(defaultName);
		setDefaultNetworkInfo(networkInfo);
	}

	@Override
	public void updateNetworkConfig() {
		NetworkInfo networkInfo = getByName(defaultNetwork.name);
		NETWORKS = getNetworks();
		if(networkInfo != null) {
			for(NetworkInfo info : NETWORKS) {
				if(info.rpcServerUrl.equals(networkInfo.rpcServerUrl)) {
					setDefaultNetworkInfo(info);
					return;
				}
			}
		}
	}
	private void networkChanged(NetworkInfo networkInfo) {
		for (OnNetworkChangeListener listener : onNetworkChangedListeners) {
			listener.onNetworkChanged(networkInfo);
		}
	}
}
