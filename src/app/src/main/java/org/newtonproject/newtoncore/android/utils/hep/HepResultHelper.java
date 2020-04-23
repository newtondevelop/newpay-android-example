package org.newtonproject.newtoncore.android.utils.hep;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newtonproject.newtoncore.android.data.entity.hepnode.ErrorBody;
import org.newtonproject.newtoncore.android.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-06-27--15:12
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class HepResultHelper {
    private static Gson gson = StringUtil.getGsonWithoutNull();
    private static String HEP_CODE = "status_code";
    private static String HEP_RESULT = "result";
    private static String HEP_ERROR = "errors";
    private static String HEP_MESSAGE = "message";

    public static JSONObject generateSuccessResult(Object object) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(HEP_CODE, 200);
        map.put(HEP_RESULT, gson.fromJson(gson.toJson(object), HashMap.class));
        return new JSONObject(map);
    }

    public static JSONObject generateSuccessResult(String jsonString) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(HEP_CODE, 200);
        map.put(HEP_RESULT, gson.fromJson(jsonString, HashMap.class));
        return new JSONObject(map);
    }

    public static JSONObject generateErrorResult(String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(HEP_CODE, 400);
        map.put(HEP_MESSAGE, message);
        return new JSONObject(map);
    }

    public static JSONObject generateErrorDetailResult(String message, ArrayList<ErrorBody.ErrorItem> errorList) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(HEP_CODE, 400);
        map.put(HEP_MESSAGE, message);
        map.put(HEP_ERROR, gson.fromJson(gson.toJson(errorList), JSONArray.class));
        return new JSONObject(map);
    }
}
