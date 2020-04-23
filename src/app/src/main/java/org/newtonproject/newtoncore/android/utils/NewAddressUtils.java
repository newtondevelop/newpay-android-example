package org.newtonproject.newtoncore.android.utils;

import org.newtonproject.newtoncore.android.C;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NewAddressUtils {

    public static final int CHAIN_ID = C.CURRENT_CHAIN_ID;
    public static final String PREFIX = "NEW";
    public static final String NEWID_PREFIX = "NEWID";

    /**
     * ByteArray to hex String.
     *
     * @param bArray Array
     * @return HexString
     */
    public static String bytesToHexString(byte[] bArray) {

        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * add byte1 [] and byte2 []
     * @param data1
     * @param data2
     * @return
     */
    public static byte[] add(byte[] data1, byte[] data2) {
        byte[] result = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, result, 0, data1.length);
        System.arraycopy(data2, 0, result, data1.length, data2.length);
        return result;
    }

    /**
     * convert eth address to new address.
     * New Address Standard: ($chainID + ${publickey.substring(num1,num2)} + ${checksum})
     * <a href="https://gitlab.newtonproject.org/xiawu/newton-documentation/blob/master/NIP/NIP-1.md">NIP-1</a>
     * @param hexAddress String.
     * @return newAddress.
     */
    public static String hexAddress2NewAddress(String hexAddress) {
        //TODO: Maybe need substring(num1,num2) for hexChainID.
        if(hexAddress == null || hexAddress.length() == 0) {
            return null;
        }
        if(hexAddress.startsWith(PREFIX)){
            return hexAddress;
        }
        String hexPrefix = "0x";
        if(hexAddress.startsWith(hexPrefix)) {
            hexAddress = hexAddress.substring(2);
        }
        if(hexAddress.length() != 40 || !isHexNumber(hexAddress)) {
            return hexAddress;
        }
        String hexChainId = Integer.toHexString(CHAIN_ID);
        if(hexChainId.length() > 8) {
            hexChainId = hexChainId.substring(hexChainId.length() - 8);
        }
        String data = hexChainId + hexAddress;
        byte[] addressData = new BigInteger(data, 16).toByteArray();
        return PREFIX + Base58.encodeChecked(0, addressData);
    }


    /**
     * like {@link #hexAddress2NewAddress(String)}.
     * @param newAddress new address.
     * @return eth address.
     */
    public static String newAddress2HexAddress(String newAddress) {
        // 3 is 'new' prefix, 2 is version while base58.encodeChecked(version, data)
        if(checkNewAddress(newAddress)){
            String res = bytesToHexString(Base58.decodeChecked(newAddress.substring(PREFIX.length()))).substring(2).toLowerCase();
            return "0x" + res.substring(res.length() - 40);
        }else{
            return newAddress;
        }
    }


    /**
     * check new address about prefix and checksum.
     * @param newAddress
     * @return boolean.
     */
    public static boolean checkNewAddress(String newAddress) {
        if(null == newAddress || !newAddress.startsWith(PREFIX)) return false;
        try {
            String s = bytesToHexString(Base58.decodeChecked(newAddress.substring(PREFIX.length()))).substring(2);
            String chainId = s.substring(0, s.length() - 40);
            Integer integer = Integer.valueOf(chainId, 16);
            return integer == CHAIN_ID;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * generateQrString
     * @param address wallet address
     * @param amount wallet amount
     * @return qr string
     */
    public static String generateQrString(String address, String amount) {
        if(address != null && amount != null) {
            return "newton:" + hexAddress2NewAddress(address) + "?amount=" + amount;
        }
        if(address != null) {
            return address;
        }
        return null;
    }

    /**
     * generate newid from address, maybe new address or eth address.
     * @param pub String.
     * @return newid.
     */
    public static String generateNewId(String pub) {
        String hash = Hash.sha3(pub);
        System.out.println("hash:" + hash);
        hash = Numeric.cleanHexPrefix(hash);
        String hexChainId = Integer.toHexString(CHAIN_ID);
        if(hexChainId.length() > 8) {
            hexChainId = hexChainId.substring(hexChainId.length() - 8);
        }
        String data = hexChainId + hash;
        byte[] addressData = new BigInteger(data, 16).toByteArray();
        return NEWID_PREFIX + Base58.encodeChecked(0, addressData);
    }

    public static boolean isHexNumber(String str) {
        if(str != null && str.length() > 0) {
            if(str.startsWith("0x")) str = str.replace("0x", "");
            for(int i = 0; i < str.length(); i ++) {
                char c = str.charAt(i);
                if(('0' <= c && c <= '9') || ('A' <= c && c <= 'F') || ('a' <= c && c <= 'f')) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
