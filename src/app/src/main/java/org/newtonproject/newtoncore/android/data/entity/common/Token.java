package org.newtonproject.newtoncore.android.data.entity.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Token implements Parcelable {
    public final TokenInfo tokenInfo;
    public final BigDecimal balance;

    public Token(TokenInfo tokenInfo, BigDecimal balance) {
        this.tokenInfo = tokenInfo;
        this.balance = balance;
    }

    private Token(Parcel in) {
        tokenInfo = in.readParcelable(TokenInfo.class.getClassLoader());
        balance = new BigDecimal(in.readString());
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(tokenInfo, flags);
        dest.writeString(balance.toString());
    }
}
