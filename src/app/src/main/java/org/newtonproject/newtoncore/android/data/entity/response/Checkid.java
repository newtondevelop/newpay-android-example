package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/10--5:13 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Checkid {
    @SerializedName("is_exist")
    public boolean isExist;
    @SerializedName("is_show_exchange_rate")
    public boolean isShowExchangeRate;
}
