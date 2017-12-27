package com.glodon.bim.basic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * 描述：
 * 作者：zhourf on 2017/9/21
 * 邮箱：zhourf@glodon.com
 */

public class BitmapUtil {

    /**
     * 计算Bitmap的大小
     * return   字节数
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * base64编码的字符串转为bitmap
     */
    public static Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray=android.util.Base64.decode(string, android.util.Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
