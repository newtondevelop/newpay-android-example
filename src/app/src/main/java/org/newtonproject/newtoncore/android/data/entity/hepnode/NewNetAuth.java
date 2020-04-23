package org.newtonproject.newtoncore.android.data.entity.hepnode;

import com.google.gson.annotations.SerializedName;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-06-03--19:17
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NewNetAuth {
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
    @SerializedName("sign_type")
    public String signType;
    @SerializedName("action")
    public String action;
    @SerializedName("scope")
    public String scope;
    @SerializedName("expired")
    public String expired;
    @SerializedName("memo")
    public String memo;
    @SerializedName("language")
    public String language;
    @SerializedName("os")
    public String os;

    @Override
    public String toString() {
        return "NewNetAuth{" +
                "uuid='" + uuid + '\'' +
                ", dAppId='" + dappId + '\'' +
                ", protocol='" + protocol + '\'' +
                ", version='" + version + '\'' +
                ", timestamp=" + timestamp +
                ", nonce='" + nonce + '\'' +
                ", signature='" + signature + '\'' +
                ", action='" + action + '\'' +
                ", scope=" + scope +
                ", expired=" + expired +
                ", memo='" + memo + '\'' +
                '}';
    }
}
