package org.newtonproject.newtoncore.android.data.entity.common;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TokenInfo implements Parcelable {
    public final String address;
    public final String name;
    public final String symbol;
    public final int decimals;

    public TokenInfo(String address, String name, String symbol, int decimals) {
        this.address = address;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
    }

    private TokenInfo(Parcel in) {
        address = in.readString();
        name = in.readString();
        symbol = in.readString();
        decimals = in.readInt();
    }

    public static final Creator<TokenInfo> CREATOR = new Creator<TokenInfo>() {
        @Override
        public TokenInfo createFromParcel(Parcel in) {
            return new TokenInfo(in);
        }

        @Override
        public TokenInfo[] newArray(int size) {
            return new TokenInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeInt(decimals);
    }
}
