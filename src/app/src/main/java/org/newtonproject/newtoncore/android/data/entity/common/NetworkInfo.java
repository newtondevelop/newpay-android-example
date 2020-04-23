package org.newtonproject.newtoncore.android.data.entity.common;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NetworkInfo {
    public final String name;
    public final String symbol;
    public final String rpcServerUrl;
    public final String explorerUrl;
    public final int chainId;
    public final boolean isMainNetwork;
    public boolean isAccessed;

    public NetworkInfo(
            String name,
            String symbol,
            String rpcServerUrl,
            String explorerUrl,
            int chainId,
            boolean isMainNetwork) {
        this.name = name;
        this.symbol = symbol;
        this.rpcServerUrl = rpcServerUrl;
        this.explorerUrl = explorerUrl;
        this.chainId = chainId;
        this.isMainNetwork = isMainNetwork;
    }

    public void setAccessed(boolean accessed) {
        isAccessed = accessed;
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", rpcServerUrl='" + rpcServerUrl + '\'' +
                ", explorerUrl='" + explorerUrl + '\'' +
                ", chainId=" + chainId +
                ", isMainNetwork=" + isMainNetwork +
                '}';
    }
}
