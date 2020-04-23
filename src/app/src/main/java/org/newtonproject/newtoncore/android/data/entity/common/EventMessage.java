package org.newtonproject.newtoncore.android.data.entity.common;

/**
 * Package: org.newtonproject.newpay.android.data.entity
 * Author: weixuefeng@lubangame.com
 * Time: 2018/9/25--PM 4:08
 */
public class EventMessage<T> {

    public final static int OBJECT = 0;
    public final static int AVATAR_CHANGED_EVENT = 1;
    public final static int COUNTRY_CODE_CHANGED_EVENT = 2;
    public final static int PROFILE_EVENT = 3;
    public final static int FLAG_EVENT = 4;
    public final static int REGISTER_EVENT = 5;
    public final static int WALLET_EVENT = 6;
    public final static int NEWFORCE_EVENT = 7;
    public final static int NODE_CANDITION_EVENT = 8;
    public final static int NODE_PROFILE_EVENT = 9;
    public final static int NODE_ID = 10;
    public final static int ASSET_EVENT = 11;
    public final static int POCKET_SHARE_EVENT = 12;
    // hep
    public final static int NEWNET_CACHE_AUTH = 13;
    public final static int NEWNET_CACHE_PAY = 14;
    public final static int NEWNET_CACHE_PROOF = 15;
    // dApp detail
    public final static int DAPP_DETAIL = 16;
    // change tab
    public final static int CHANGE_TAB_ECOLOGY = 17;
    // hep h5
    public final static int NEWNET_AUTH_LOGIN =18;
    public final static int NEWNET_AUTH_PAY = 19;
    public final static int NEWNET_AUTH_PROOF = 20;

    public int type;
    public T message;

    public EventMessage(int type, T message) {
        this.type = type;
        this.message = message;
    }
}

