package org.newtonproject.newtoncore.android.utils;

import android.util.Log;

import org.newtonproject.newtoncore.android.BuildConfig;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/29--PM 9:42
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Logger {
    private static boolean isDebug = BuildConfig.DEBUG;
    private static String TAG = "newpayLogger";
    
    public static void d(String TAG, String message) {
        if(isDebug) {
            Log.d(TAG, getMessage(message));
        }
    }

    public static void d(String message) {
        if(isDebug) {
            Log.d(TAG, getMessage(message));
        }
    }

    public static void e(String TAG, String message) {
        if(isDebug) {
            Log.e(TAG, getMessage(message));
        }
    }

    public static void e(String message) {
        if(isDebug) {
            Log.e(TAG, getMessage(message));
        }
    }

    public static void i(String TAG, String message) {
        if(isDebug) {
            Log.i(TAG, getMessage(message));
        }
    }

    public static void i(String message) {
        if(isDebug) {
            Log.i(TAG, getMessage(message));
        }
    }

    public static void w(String TAG, String message) {
        if(isDebug) {
            Log.w(TAG, getMessage(message));
        }
    }

    public static void w(String message) {
        if(isDebug) {
            Log.w(TAG, getMessage(message));
        }
    }
    
    private static String getMessage(String message) {
        return String.format("\r\n------------>logger<-----------\r\n---------->%s<----------- \r\n%s", DateUtils.getDateTime(System.currentTimeMillis() / 1000), message);
    }
}
