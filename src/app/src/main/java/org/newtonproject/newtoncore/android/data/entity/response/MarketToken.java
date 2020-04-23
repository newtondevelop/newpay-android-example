package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/3/4--10:15 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class MarketToken {

    @SerializedName("symbol")  // NEW
    public String symbol;
    @SerializedName("name")
    public String name = "Newton";
    @SerializedName("quote")
    public ArrayList<Quote> quotes;

    public class Quote {
        @SerializedName("symbol")  // CNY | BTC
        public String symbol;
        @SerializedName("price")
        public float price;
    }

}
