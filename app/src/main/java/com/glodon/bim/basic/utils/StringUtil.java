package com.glodon.bim.basic.utils;

/**
 * 描述：字符串操作
 * 作者：zhourf on 2017/9/19
 * 邮箱：zhourf@glodon.com
 */

public class StringUtil {
    /**
     * 截取4个汉子或8个字符 再跟上...
     */
    public static String getString(String title) {
        if (title.length() <= 4) {
            return title;
        } else {
            String target = "";
            for (int i = 4; i < title.length(); i++) {
                String tempStr = title.substring(0, i);
                if (tempStr.length() > 8) {
                    break;
                }
                if (tempStr.getBytes().length > 12) {
                    break;
                }
                target = tempStr + "...";
            }
            return target;
        }
    }
}
