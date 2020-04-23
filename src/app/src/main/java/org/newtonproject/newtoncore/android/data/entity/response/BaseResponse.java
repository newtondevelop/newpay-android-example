package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * Package: org.newtonproject.newpay.android.data.entity
 * Author: weixuefeng@lubangame.com
 * Time: 2018/9/25--PM 8:53
 */
public class BaseResponse<T> {

    @SerializedName("error_code")
    public int errorCode;
    @SerializedName("result")
    public T result;
    @SerializedName("error_message")
    public String errorMessage;
    @Override
    public String toString() {
        return String.format("{ errorCode: %s, \r\n errorMessage: %s, \r\n result: %s", errorCode, errorMessage, result == null ? "null" : result.toString());
    }
}
