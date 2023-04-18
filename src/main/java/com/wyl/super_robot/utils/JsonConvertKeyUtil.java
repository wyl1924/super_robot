package com.wyl.super_robot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * @author wyl
 * @date 2023/4/14 15:07
 */
public class JsonConvertKeyUtil {

    public static JSONObject covertObject(JSONObject object, boolean toUp) {
        if (object == null) {
            return null;
        }
        JSONObject newObject = new JSONObject();
        Set<String> set = object.keySet();
        for (String key : set) {
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                // 数组
                value = covertArray(object.getJSONArray(key), toUp);
            } else if (value instanceof JSONObject) {
                // 对象

                value = covertObject(object.getJSONObject(key), toUp);
            }
            // 这个方法自己写的改成驼峰，也可以改成大写小写
            if (toUp) {
                key = stringUtilMax(key);
            } else {
                key = stringUtilMin(key);
            }

            newObject.put(key, value);
        }
        return newObject;
    }

    public static JSONArray covertArray(JSONArray array, boolean toUp) {
        if (array == null) {
            return null;
        }
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                // 数组
                value = covertArray(array.getJSONArray(i), toUp);
            } else if (value instanceof JSONObject) {
                // 对象
                value = covertObject(array.getJSONObject(i), toUp);
            }
            newArray.add(value);
        }
        return newArray;
    }

    public static String convertJSONKeyRetrunString(String jsonStr, boolean toUp) {
        if ("[".equals(jsonStr.substring(0, 1))) {
            JSONArray jsonObject = JSONArray.parseArray(jsonStr);
            JSONArray jsonResult = covertArray(jsonObject, toUp);
            return jsonResult.toJSONString();
        }
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        JSONObject jsonResult = covertObject(jsonObject, toUp);
        return jsonResult.toJSONString();

    }

    public static String convertJSONKeyRetrunString(Object jsonStr, boolean toUp) {
        Object jsonObject = JSON.toJSON(jsonStr);
        if (jsonObject.getClass().getName().indexOf("Array") > -1) {
            JSONArray jsonResult = covertArray((JSONArray) jsonObject, toUp);
            return jsonResult.toJSONString();
        } else {
            JSONObject jsonResult = covertObject((JSONObject) jsonObject, toUp);
            return jsonResult.toJSONString();
        }
    }

    public static String stringUtilMax(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }

        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String stringUtilMin(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

}

