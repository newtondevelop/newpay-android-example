package org.newtonproject.newtoncore.android.data.entity.common;

import com.google.gson.annotations.SerializedName;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionNotification {
    @SerializedName("msg_type")
    public long msg_type;

    @SerializedName("txid")
    public String txid;

    @SerializedName("blockheight")
    public long blockheight;

    @SerializedName("from_address")
    public String from_address;

    @SerializedName("to_address")
    public String to_address;

    @SerializedName("value")
    public String value;

    @SerializedName("time")
    public long time;

    public boolean readed = false;


    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public long getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(long msg_type) {
        this.msg_type = msg_type;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public long getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(long blockheight) {
        this.blockheight = blockheight;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
