package org.newtonproject.newtoncore.android.data.entity.common;

import android.text.TextUtils;

import java.util.regex.Pattern;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Address {

    private static final Pattern ignoreCaseAddrPattern = Pattern.compile("(?i)^(0x)?[0-9a-f]{40}$");
    private static final Pattern lowerCaseAddrPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$");
    private static final Pattern upperCaseAddrPattern = Pattern.compile("^(0x)?[0-9A-F]{40}$");

    public final String value;

    public Address(String value) {
        this.value = value;
    }

    public static boolean isAddress(String address) {
        return !(TextUtils.isEmpty(address) || !ignoreCaseAddrPattern.matcher(address).find())
                && (lowerCaseAddrPattern.matcher(address).find() || upperCaseAddrPattern.matcher(address).find());
    }
}
