package org.newtonproject.newtoncore.android.utils;

import android.text.TextUtils;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.newtonproject.web3j.utils.Numeric;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Validators {

    public static boolean passwordValidatorNum(String password) {
        return password.length() == 6;
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean validatePrivateKey(String privkey) {
        String s = Numeric.cleanHexPrefix(privkey);
        return s.length() == 64;
    }

    public static boolean validateComment(String comment) {
        if(TextUtils.isEmpty(comment)) return true;
        if(comment.length() > 64) return false;
        return true;
    }

    public static boolean isHexString(String hexStr) {
        hexStr = Numeric.cleanHexPrefix(hexStr);
        String validate = "(?i)[0-9a-f]+";
        return hexStr.matches(validate);
    }

    public static boolean checkPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8 && password.length() <= 20;
    }

    public static boolean checkMnemonic(String mnemonic) {
        try {
            MnemonicCode.INSTANCE.check(StringUtil.getMnemonicFromString(mnemonic));
            return true;
        } catch (MnemonicException e) {
            e.printStackTrace();
        }
        return false;
    }
}
