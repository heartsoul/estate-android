package com.glodon.bim.basic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 描述：文件操作
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class FileUtils {
    /**
     * 判断SD卡是否存在
     *
     * @return boolean
     */
    public static boolean isSDExist() {
        return Environment.getExternalStorageDirectory().exists();
    }

    /**
     * 保存图片
     *
     * @param context 持有的context
     * @param url 图片地址
     * @param ext 图片后缀
     * @return boolean
     */
//    public static boolean saveImage(Context context, String url, String ext) {
//        if (TextUtils.isEmpty(url)) {
//            return false;
//        }
//        String filePath = url;
//        File dir = new File(CommonFilePathConfig.DIR_APP_CACHE_CAMERA);
//        if (!dir.exists())
//            dir.mkdirs();
//        long timeMillis = System.currentTimeMillis();
//        int result = FileUtils.copyFile(dir.getAbsolutePath(), "save" + timeMillis, ext,
//                FileUtils.readFlieToByte(filePath, 0, FileUtils.decodeFileLength(filePath)));
//        if (result == 0) {
//            CameraUtils.refreshingMediaScanner(AppContextUtils.getAppContext(),
//                    dir.getAbsolutePath() + "/save" + timeMillis + ext);
//            ToastUtil.showTextViewPrompt(AppContextUtils.getAppContext(),
//                    context.getString(R.string.save_to_local_space) + dir.getAbsolutePath());
//            return false;
//        }
//        ToastUtil.showTextViewPrompt(AppContextUtils.getAppContext(),
//                context.getString(R.string.save_to_local_fail));
//        return true;
//    }

    /**
     * 保存图片
     *
     * @param mContext 持有 context
     * @param bitmap 图片的bitmap
     */
//    public static boolean saveImage(Context mContext, Bitmap bitmap) {
//        if (!FileUtils.checkSDCard()) {
//            ToastUtil.showTextViewPrompt(AppContextUtils.getAppContext(),
//                    AppContextUtils.getAppContext().getString(R.string.sdcard_not_exist));
//            return false;
//        }
//        File dir = new File(CommonFilePathConfig.DIR_APP_CACHE_CAMERA);
//        if (!dir.exists())
//            dir.mkdirs();
//        long timeMillis = System.currentTimeMillis();
//        String imgName = "qrcode" + String.valueOf(timeMillis) + ".png";
//
//        File file = new File(CommonFilePathConfig.DIR_APP_CACHE_CAMERA, imgName);
//        FileOutputStream outStrem = null;
//        try {
//            outStrem = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStrem);
//            outStrem.flush();
//            outStrem.close();
//            outStrem = null;
//
//            // 保存图片到系统图库
//            String newPath = SystemTool.getAlbumPath() + "/" + imgName;
//            if (FileTool.saveFile(file.getAbsolutePath(), newPath)) {
//                CameraUtils.refreshingMediaScanner(AppContextUtils.getAppContext(), newPath);
//            } else {
//                CameraUtils.refreshingMediaScanner(AppContextUtils.getAppContext(),
//                        dir.getAbsolutePath() + "/" + imgName);
//            }
//            ToastUtil.showTextViewPrompt(AppContextUtils.getAppContext(),
//                    mContext.getString(R.string.save_to_local_space) + dir.getAbsolutePath());
//            return true;
//        } catch (Exception Ex) {
//            return false;
//        } finally {
//            try {
//                if (outStrem != null) {
//                    outStrem.flush();
//                    outStrem.close();
//                }
//            } catch (IOException ioEx) {
//                ioEx.printStackTrace();
//            }
//        }
//    }

    /**
     * decode file length
     *
     * @param filePath 文件路径
     */
    public static int decodeFileLength(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        return (int) file.length();
    }

    /**
     * 把文件读成二进制
     */
    public static byte[] readFlieToByte(String filePath, int seek, int length) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        if (length == -1) {
            length = (int) file.length();
        }

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bs = new byte[length];
            randomAccessFile.seek(seek);
            randomAccessFile.readFully(bs);
            randomAccessFile.close();
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @return 检查sdcard 空间大于500kb true
     */
    public static Boolean checkSDCard() {
        if (!isSDExist()) {
            return false;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (moreSDAvailableSpare()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * @return sdcard 空间
     */
    public static boolean moreSDAvailableSpare() {
        File path = Environment.getExternalStorageDirectory();
        StatFs statfs = new StatFs(path.getPath());
        long blocSize = statfs.getBlockSize(); // byte
        long totalBlocks = statfs.getBlockCount();
        long availaBlock = statfs.getAvailableBlocks();
        long total = totalBlocks * blocSize;
        long availale = availaBlock * blocSize;
        int arrMemorySD = (int) ((total - availale) / 1024); // kb
        return arrMemorySD > 500; // 500 kb
    }

}
