package org.newtonproject.newtoncore.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import org.newtonproject.newtoncore.android.data.repository.SharedPreferenceRepository;

import java.util.List;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class PhoneManager {

    private static final String TAG = "PhoneManager";

    private static String getIMEI(Context context) {
        SharedPreferenceRepository sp = new SharedPreferenceRepository(context);
        return sp.getIMEI();
    }

    private static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (mac != null) {
            return mac;
        }
        return null;
    }

    public static String getSessionID(Context context) {
        String imei = getIMEI(context);
        if (imei != null && !TextUtils.isEmpty(imei)) {
            return imei;
        }
        String mac = getLocalMacAddress(context);
        if (mac != null && !TextUtils.isEmpty(mac)) {
            return mac;
        }
        return "undefine";
    }

    public static void tryLaunchDialPhoneNumberIntent(Context paramContext, String paramString) {
        Intent localIntent = new Intent(Intent.ACTION_DIAL);
        localIntent.setData(Uri.parse("tel:" + paramString));
        List<ResolveInfo> localList = paramContext.getPackageManager().queryIntentActivities(localIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if ((localList != null) && (localList.size() > 0))
            paramContext.startActivity(localIntent);
    }
}
