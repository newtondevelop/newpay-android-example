package org.newtonproject.newtoncore.android.data.entity.hepnode;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-06-15--17:57
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ErrorBody {

    @SerializedName("errors")
    public ArrayList<ErrorItem> errors;

    public static class ErrorItem {
        @SerializedName("name")
        public String name;
        @SerializedName("message")
        public String message;
    }

    public String getMessage() {
        if(errors.size() == 0) {
            return "";
        }
        return errors.get(0).message;
    }
}
