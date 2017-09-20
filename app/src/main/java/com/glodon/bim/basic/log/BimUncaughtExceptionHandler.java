package com.glodon.bim.basic.log;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.glodon.bim.main.BaseApplication;
import com.glodon.bim.basic.config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

/**
 * 描述：app崩溃异常记录
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public class BimUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static BimUncaughtExceptionHandler inst;
    private static final String TAG = BimUncaughtExceptionHandler.class.getName();

    public static BimUncaughtExceptionHandler getInstance() {
        if (inst == null) inst = new BimUncaughtExceptionHandler();
        return inst;
    }

    private Context mContext;
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    private JSONObject jsonObject;

    private BimUncaughtExceptionHandler() {
    }

    public void init(Context context) {
        mContext = context;
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handleUncaughtException(throwable);
        if (throwable != null) LogUtil.w(TAG, throwable);
        mExceptionHandler.uncaughtException(thread, throwable);
    }

    private void handleUncaughtException(Throwable ex) {
        if (ex == null) return;
        try {
            if (jsonObject == null) jsonObject = new JSONObject();
            saveDeviceInfo();
            saveForceCloseInfo2File(ex);
            // 把异常信息发送到服务器
            //ForceCloseFeedBack.getInstance().feedBack(mContext, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保存系统信息
    private void saveDeviceInfo() throws JSONException {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        jsonObject.put("platform", "android");
        jsonObject.put("model", Build.MODEL);
//        jsonObject.put("trackid", AppConfig.PARTNER_ID);
//        jsonObject.put("product", AppConfig.CLIENT_TAG);
        jsonObject.put("os_version", Build.VERSION.RELEASE);
//        jsonObject.put("deviceid", DeviceInfo.getDeviceId());
        jsonObject.put("net_type", tm.getNetworkOperator());
        jsonObject.put("timestamp", System.currentTimeMillis());
//        jsonObject.put("app_version", Application.getInstance().getVersionCode());
    }

    //保存异常信息到文件
    private void saveForceCloseInfo2File(Throwable ex) throws Exception {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString() + "\n";
        jsonObject.put("errorDetail", result);

        if (AppConfig.LOG_ERR_SAVE) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = sDateFormat.format(new java.util.Date());
            String dir = AppConfig.BIM_LOG_DIRECTORY;
            File dirs = new File(dir);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            String path = dir + "/bimError" + date + ".txt";
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(jsonObject.toString().getBytes());
            fos.close();

            //扫描文件  否则生成后无法在手机内显示
            MediaScannerConnection.scanFile(BaseApplication.getInstance(), new String[]{path}, new String[]{"application/octet-stream"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(final String path, final Uri uri) {
                    //your file has been scanned!
                }
            });
        }
    }

}
