package org.newtonproject.newtoncore.android.utils;

import android.text.TextUtils;

import org.newtonproject.newtoncore.android.C;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class BalanceUtils {
    private static String weiInEth  = "1000000000000000000";



    public static BigDecimal weiToEth(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
    }

    public static String weiToEth(BigInteger wei, int sigFig) throws Exception {
        BigDecimal eth = weiToEth(wei);
        int scale = sigFig - eth.precision() + eth.scale();
        BigDecimal eth_scaled = eth.setScale(scale, RoundingMode.HALF_UP);
        return eth_scaled.toString();
    }

    public static String weiToNEW(BigInteger wei) {
        BigDecimal eth = weiToEth(wei);
        BigDecimal eth_scaled = eth.setScale(C.NEW_PRECISION, RoundingMode.HALF_UP);
        String res = eth_scaled.toString();
        res = divideZero(res);
        return res;
    }

    public static String weiToNEW(String wei) {
        if(TextUtils.isEmpty(wei)) {
            wei = "0";
        }
        BigInteger integer = new BigInteger(wei);
        return weiToNEW(integer);
    }

    public static String getStandardNewForShow(String amountNew) {
        return weiToNEW(new BigInteger(BalanceUtils.EthToWei(amountNew)));
    }

    public static String adjustNewForce(String nf) {
        BigInteger bigNf = new BigInteger(BalanceUtils.EthToWei(nf));
        BigDecimal eth = weiToEth(bigNf);
        BigDecimal eth_scaled = eth.setScale(2, RoundingMode.FLOOR);
        String res = eth_scaled.toString();
        res = divideZero(res);
        return res;
    }

    public static String weiToNEWAccurate(BigInteger wei) {
        BigDecimal eth = weiToEth(wei);
        BigDecimal eth_scaled = eth.setScale(eth.precision(), RoundingMode.FLOOR);
        String res = eth_scaled.toString();
        res = divideZero(res);
        return res;
    }

    public static String divideZero(String str) {
        if(null == str) {
            return null;
        }
        String res = str;
        if(str.endsWith(".")) {
            res = str.replace(".","");
            return res;
        }
        if(str.startsWith(".")) {
            return "0" + str;
        }
        if(str.contains(".") && str.endsWith("0")) {
            res = str.substring(0, str.length() - 1);
            return divideZero(res);
        }else{
            return res;
        }
    }

    public static String ethToUsd(String priceUsd, String ethBalance) {
        BigDecimal usd = new BigDecimal(ethBalance).multiply(new BigDecimal(priceUsd));
        usd = usd.setScale(2, RoundingMode.CEILING);
        return usd.toString();
    }

    public static String EthToWei(String eth){
        if(TextUtils.isEmpty(eth)) {
            return "0";
        }
        BigDecimal wei = new BigDecimal(eth).multiply(new BigDecimal(weiInEth));
        return wei.toBigInteger().toString();
    }

    public static BigDecimal weiToGweiBI(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.GWEI);
    }

    public static String weiToGwei(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.GWEI).toPlainString();
    }

    public static BigInteger gweiToWei(BigDecimal gwei) {
        return Convert.toWei(gwei, Convert.Unit.GWEI).toBigInteger();
    }

    /**
     * Base - taken to mean default unit for a currency e.g. ETH, DOLLARS
     * Subunit - taken to mean subdivision of base e.g. WEI, CENTS
     *
     * @param baseAmountStr - decimal amonut in base unit of a given currency
     * @param decimals - decimal places used to convert to subunits
     * @return amount in subunits
     */
    public static BigInteger baseToSubunit(String baseAmountStr, int decimals) {
        assert(decimals >= 0);
        BigDecimal baseAmount = new BigDecimal(baseAmountStr);
        BigDecimal subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(decimals));
        try {
            return subunitAmount.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            assert(false);
            return subunitAmount.toBigInteger();
        }
    }

    /**
     * @param subunitAmount - amount in subunits
     * @param decimals - decimal places used to convert subunits to base
     * @return amount in base units
     */
    public static BigDecimal subunitToBase(BigInteger subunitAmount, int decimals) {
        assert(decimals >= 0);
        return new BigDecimal(subunitAmount).divide(BigDecimal.valueOf(10).pow(decimals));
    }

    public static BigInteger NEWToISAAC(String baseAmountStr) {
        int decimals = 18;
        BigDecimal baseAmount = new BigDecimal(baseAmountStr);
        BigDecimal subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(decimals));
        try {
            return subunitAmount.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            assert(false);
            return subunitAmount.toBigInteger();
        }
    }

    public static BigDecimal isaacToNEW(BigInteger subunitAmount) {
        int decimals = 18;
        return new BigDecimal(subunitAmount).divide(BigDecimal.valueOf(10).pow(decimals));
    }


}
