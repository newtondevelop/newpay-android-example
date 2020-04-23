package org.newtonproject.newtoncore.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class CheckUtils {

    public static int checkTransactionStatus(String walletAddress, String from, String to) {
        if(TextUtils.isEmpty(walletAddress)) {
            throw new NullPointerException("wallet address can not be null");
        }
        if(TextUtils.isEmpty(from)) {
            throw new NullPointerException("from can not be null");
        }
        if(TextUtils.isEmpty(to)) {
            throw new NullPointerException("to can not be null");
        }
        if(walletAddress.equals(from) && from.equals(to)) {
            return C.TRANSACTION_STATUS.MOVED;
        }
        if(walletAddress.equals(from) && !from.equals(to)) {
            return C.TRANSACTION_STATUS.SEND;
        }
        if(walletAddress.equals(to) && !from.equals(to)) {
            return C.TRANSACTION_STATUS.RECEIVE;
        }
        return 0;
    }

    public static void checkPermissionForApp(Activity activity) {
        int requestPhoneState = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (requestPhoneState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, C.PERMISSION_CODE_READ_PHONE_STATE);
        } else {
            TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if(tm == null) return;
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                new SharedPreferenceRepository(activity).setIMEI(imei);
            }
        }
    }

}