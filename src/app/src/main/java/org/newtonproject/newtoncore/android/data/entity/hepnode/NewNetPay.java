package org.newtonproject.newtoncore.android.data.entity.hepnode;

import com.google.gson.annotations.SerializedName;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-06-03--19:18
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NewNetPay {
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("dapp_id")
    public String dappId;
    @SerializedName("dapp_key")
    public String dappKey;

    @SerializedName("protocol")
    public String protocol;
    @SerializedName("version")
    public String version;
    @SerializedName("ts")
    public String timestamp;
    @SerializedName("nonce")
    public String nonce;
    @SerializedName("signature")
    public String signature;
    @SerializedName("action")
    public String action;
    @SerializedName("expired")
    public String expired;
    @SerializedName("sign_type")
    public String signType;
    @SerializedName("description")
    public String description;
    @SerializedName("price_currency")
    public String currency;
    @SerializedName("total_price")
    public String totalPrice;
    @SerializedName("order_number")
    public String orderNumber;
    @SerializedName("seller")
    public String seller;
    @SerializedName("customer")
    public String customer;
    @SerializedName("broker")
    public String broker;
    @SerializedName("language")
    public String language;
    @SerializedName("os")
    public String os;

    @Override
    public String toString() {
        return "NewNetPay{" +
                "uuid='" + uuid + '\'' +
                ", dAppId='" + dappId + '\'' +
                ", protocol='" + protocol + '\'' +
                ", version='" + version + '\'' +
                ", timestamp=" + timestamp +
                ", nonce='" + nonce + '\'' +
                ", signature='" + signature + '\'' +
                ", action='" + action + '\'' +
                ", expired=" + expired +
                ", signType='" + signType + '\'' +
                ", description='" + description + '\'' +
                ", currency='" + currency + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", seller='" + seller + '\'' +
                ", customer='" + customer + '\'' +
                ", broker='" + broker + '\'' +
                '}';
    }
}
