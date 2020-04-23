package org.newtonproject.newtoncore.android.utils;

import org.newtonproject.newtoncore.android.data.entity.common.ScanResultInfo;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ScanUtils {
    public static String NEW_PREFIX = "NEW";
    private static String BITCOIN_PREFIX = "bitcoin";

    public static ScanResultInfo parseScanResult(String scanResult) {
        String[] scanArray = scanResult.split("\\?");
        String addressInfo = scanArray[0];
        String[] addressArray = addressInfo.split(":");
        if(scanArray.length > 1) {
            String amountInfo = scanArray[1];
            String addressType = addressArray[0];
            String address = addressArray[1];
            String[] amountArray = amountInfo.split("=");
            String amount = amountArray[1];
            boolean b = NewAddressUtils.checkNewAddress(address);
            if(b) {
                return new ScanResultInfo(addressType, address, amount);
            }else {
                return null;
            }
        }
        if(addressArray.length > 1) {
            String addressType = addressArray[0];
            String address = addressArray[1];
            boolean b = NewAddressUtils.checkNewAddress(address);
            if(b) {
                return new ScanResultInfo(addressType, address);
            }else {
                return null;
            }
        }
        if(scanResult.startsWith(NEW_PREFIX)){
            String addressType = "newton";
            String address = addressArray[0];
            boolean b = NewAddressUtils.checkNewAddress(address);
            if(b) {
                return new ScanResultInfo(addressType, address);
            }else {
                return null;
            }
        }
        return null;
    }
}
