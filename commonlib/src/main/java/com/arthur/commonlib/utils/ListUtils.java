package com.arthur.commonlib.utils;

import android.text.TextUtils;

import java.util.List;

/**
 * 列表集合工具
 */
public class ListUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * ["","","","",""]
     *
     * @param list
     * @return
     */
    public static boolean isEachEmpty(List<String> list) {
        if (list == null || list.size() == 0) {
            return true;
        }

        boolean result = true;
        for (String string : list) {
            if (!TextUtils.isEmpty(string)) {
                result = false;
                break;
            }
        }
        return result;
    }
}
