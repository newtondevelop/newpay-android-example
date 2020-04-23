package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-07-16--17:02
 * @description
 * @copyright (c) 2019 Newton Foundation. All rights reserved.
 */
public class DEXInfo {
    @SerializedName("symbol")
    public String symbol;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("dex_address")
    public String dexAddress;
    @SerializedName("min_transfer")
    public String minTransferAmount;
    @SerializedName("min_withdraw")
    public String minWithDrawAmount;
    @SerializedName("extra_fee")
    public String transferFeeAmount;
    @SerializedName("announcement_url")
    public String announcementUrl;

    @Override
    public String toString() {
        return "DEXInfo{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", dexAddress='" + dexAddress + '\'' +
                ", minTransferAmount='" + minTransferAmount + '\'' +
                ", minWithDrawAmount='" + minWithDrawAmount + '\'' +
                ", transferFeeAmount='" + transferFeeAmount + '\'' +
                ", announcementUrl='" + announcementUrl + '\'' +
                '}';
    }
}
