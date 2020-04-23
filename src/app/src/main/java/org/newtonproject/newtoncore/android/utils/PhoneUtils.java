package org.newtonproject.newtoncore.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class PhoneUtils {

    private static final String EMULATOR_IMIE = "000000000000000";

    //get installed app list on this phone

    static List<ResolveInfo> APPLIST;
    static HashMap<String, ResolveInfo> APPMAP;

    private static final String EMULATOR_PHONE_NUMBER = "15555215554";

    /**
     * fetch the list of apps on your phone and init a hashset of app packagenms
     *
     * @param context
     * @param refresh whether reuse former result
     *
     * @return
     */
    public static List<ResolveInfo> getPhoneAppListAndInitAppSet(Context context, boolean refresh) {
        getPhoneAppList(context, refresh);
        if(APPMAP == null) {
            APPMAP = new HashMap<String, ResolveInfo>();
        }
        APPMAP.clear();
        for(ResolveInfo t : APPLIST) {
            APPMAP.put(t.activityInfo.packageName, t);
        }
        return APPLIST;
    }

    /**
     * fetch the list of apps on your phone
     *
     * @param context
     * @param refresh whether reuse former result
     *
     * @return
     */
    public static List<ResolveInfo> getPhoneAppList(Context context, boolean refresh) {
        if(!refresh && APPLIST != null) {
            return APPLIST;
        }
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        APPLIST = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        return APPLIST;
    }

    /**
     * check if package is intalled in your phone
     * this method use APPSET for check so if you use this method in loop
     * it will miss the packages that intalled during that loop;
     * you can call getPhoneAppListAndInitAppSet before the loop each time
     *
     * @param mContext
     * @param packagenm
     *
     * @return
     */
    public static ResolveInfo getIntalledPackage(Context mContext, String packagenm) {
        if(APPMAP == null) {
            getPhoneAppListAndInitAppSet(mContext, true);
        }
        if(APPMAP.containsKey(packagenm)) {
            return APPMAP.get(packagenm);
        }
        return null;
    }

    private static String getMAC(Context context) {

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info != null ? info.getMacAddress() : "";
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    /**
     * getIPAddress
     *
     * @return
     */
    public static String getIPAddress() {
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        }catch(SocketException ex) {
            ex.printStackTrace();
        }
        return "127.0.0.1";
    }

    /**
     * ���֧�ֵ�SDK�汾
     *
     * @return
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @param context
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            packInfo = pm.getPackageInfo(context.getPackageName(), 0);
        }catch(NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    /**
     * @param
     *
     * @return
     */
    public static String getMobileInfo(Context mContext) {

        StringBuffer sb = new StringBuffer();
        sb.append(Build.MANUFACTURER).append("|").append(Build.MODEL).append("|").append(Build.VERSION.RELEASE);
        return sb.toString();
    }
}