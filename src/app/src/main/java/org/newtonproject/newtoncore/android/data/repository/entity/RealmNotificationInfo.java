package org.newtonproject.newtoncore.android.data.repository.entity;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class RealmNotificationInfo extends RealmObject {

    @PrimaryKey
    private String message_id;

    @Index
    private long send_time;

    private int message_type;
    private String title;
    private String body;
    private String payload;
    private String language;
    @Index
    private String txid;
    private boolean readed;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    @Override
    public String toString() {
        return String.format("{title: %s, body: %s, send_time: %s" +
                        ", message_type: %s, payload: %s, readed: %s, language: %s, txid: %s}", title,
                body, send_time, message_type, payload, readed, language, txid);
    }

    /**
     *
     @PrimaryKey
     private String txid;

     private int msg_type;
     private int blockheight;
     private String from_address;
     private String to_address;
     private String value;
     private long time;
     private boolean readed;

     public boolean isReaded() {
     return readed;
     }

     public void setReaded(boolean readed) {
     this.readed = readed;
     }

     public String getTxid() {
     return txid;
     }

     public void setTxid(String txid) {
     this.txid = txid;
     }

     public int getMsg_type() {
     return msg_type;
     }

     public void setMsg_type(int msg_type) {
     this.msg_type = msg_type;
     }

     public int getBlockheight() {
     return blockheight;
     }

     public void setBlockheight(int blockheight) {
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


     */
}
