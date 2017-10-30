package com.example.shisong.testapplication;

import android.text.TextUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class StringUtils {

    public static String encoding(String str) {
        return TextUtils.isEmpty(str) ? "" : URLEncoder.encode(str);
    }

    public static String decoding(String str) {
        return TextUtils.isEmpty(str) ? "" : URLDecoder.decode(str);
    }

    public static String toStr(Object _obj, String _defaultValue) {
        if (null == _obj || TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        return String.valueOf(_obj);
    }

    public static int toInt(Object _obj, int _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Integer.parseInt(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static final float toFloat(Object _obj, float _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Float.parseFloat(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static final double toDouble(Object _obj, double _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Double.parseDouble(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static final long toLong(Object _obj, long _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        try {
            return Long.parseLong(String.valueOf(_obj));
        } catch (Exception e) {
        }

        return _defaultValue;
    }

    public static boolean isEmptyArray(Object[] array, int len) {
        return null == array || array.length < len;
    }

    public static boolean isEmptyArray(Object[] array) {
        return isEmptyArray(array, 1);
    }

    public static boolean isEmptyList(List<?> list) {
        return null == list || list.size() < 1;
    }

    public static boolean isEmptyMap(Map<?, ?> map) {
        return null == map || map.size() < 1;
    }


    public static String cutString(String str, String beginKey, int bIndexOfPos, boolean containBeginKey,
                                   String endKey, int eIndexOfPos, boolean containEndKey) {
        if (null == str || "".equals(str))
            return null;
        try {
            int beginIndex = (null == beginKey || "".equals(beginKey)) ? 0 : bIndexOfPos;
            beginIndex = beginIndex < 0 ? 0 : beginIndex;
            int endIndex = (null == endKey || "".equals(endKey)) ? 0 : eIndexOfPos;
            endIndex = endIndex < 0 ? 0 : endIndex;
            if (beginIndex >= endIndex)
                return str;
            return str.substring(
                    containBeginKey ? beginIndex : beginKey.length() + beginIndex,
                    containEndKey ? endIndex + endKey.length() : endIndex
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String substringFromStartToKey(String string, String key, boolean containKey) {
        return cutString(string, "", 0, true, key, string.lastIndexOf(key), containKey);
    }
}
