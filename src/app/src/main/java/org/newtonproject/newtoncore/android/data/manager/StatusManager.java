package org.newtonproject.newtoncore.android.data.manager;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import org.newtonproject.newtoncore.android.BuildConfig;
import org.newtonproject.newtoncore.android.utils.StringUtil;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/5/13--2:56 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class StatusManager {

    private StatusManager() {}

    private static StatusManager mInstance;

    // get instance
    public static StatusManager getInstance() {
        if(mInstance == null) {
            synchronized (StatusManager.class) {
                if(mInstance == null) {
                    mInstance = new StatusManager();
                }
            }
        }
        return mInstance;
    }

    private String userAgent = null;
    public String getCurrentUserAgent() {
        if(TextUtils.isEmpty(userAgent)) {
            userAgent ="NewPay/" +
                    BuildConfig.VERSION_NAME + "-" +
                    BuildConfig.VERSION_CODE +
                    ";(" +
                    Build.VERSION.SDK_INT +
                    ";Android;" +
                    ")";
        }
        return userAgent;
    }

    public void initUserAgent(Context context) {
        userAgent = StringUtil.getCurrentUserAgent(context);
    }
}
