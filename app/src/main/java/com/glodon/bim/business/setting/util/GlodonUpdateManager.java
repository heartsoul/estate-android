package com.glodon.bim.business.setting.util;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.network.download.DownloadResponseHandler;
import com.glodon.bim.basic.network.download.GlodonDownloadManager;

import java.io.File;

import okhttp3.Call;

/**
 * Created by cwj on 2018/3/13.
 * Description:GlodonUpdateManager
 */

public class GlodonUpdateManager {
    String url = "http://192.168.71.72/apk/test.apk";
    private Call call;

    private static GlodonUpdateManager sInstance;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/bmMobile/download/";
    private String fileName = "";

    private GlodonUpdateManager() {

    }

    public static GlodonUpdateManager getInstance() {
        if (sInstance == null) {
            sInstance = new GlodonUpdateManager();
        }
        return sInstance;
    }

    public void showUpdateDialog(Context context) {
        new UpdateDialog(context).show();
    }

    /**
     * 通知栏显示下载
     *
     * @param context context
     * @param url     下载地址
     */
    public void download(final Context context, String url) {
        mBuilder = new NotificationCompat.Builder(context);
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        fileName = "test.apk";
        call = GlodonDownloadManager.getInstance().download(url, DOWNLOAD_PATH, fileName, new DownloadResponseHandler() {
            @Override
            public void onFinish(final File download_file) {
                Log.i("SettingActivity", "download finish " + download_file.getName());
                call = null;
                mNotifyManager.cancelAll();
                installApk(context, download_file);
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                int progress = (int) (currentBytes * 100 / totalBytes);
                Log.i("SettingActivity", "download progress: " + progress);//下载通知栏
                updateProgress(context, progress);
            }

            @Override
            public void onFailure(String error_msg) {
                Log.i("SettingActivity", "download fail " + error_msg);
            }
        });


    }

    private void updateProgress(Context context, int progress) {
        String contentText = "";
        contentText = context.getString(R.string.str_notification_download_progress, progress);

        int icon = context.getApplicationInfo().icon;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel infoChannel = new NotificationChannel("11","channel", NotificationManager.IMPORTANCE_DEFAULT);
            infoChannel.setDescription("abc");
            infoChannel.enableLights(false);
            infoChannel.enableVibration(false);
            mNotifyManager.createNotificationChannel(infoChannel);
        }

        mBuilder.setContentTitle("BIM协同").setSmallIcon(icon);
        mBuilder.setContentText(contentText)
                .setChannel("11")
                .setProgress(100, progress, false);
        // setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0,
                new Intent(), 0);
        mBuilder.setContentIntent(pendingintent);
        mBuilder.setAutoCancel(true);
        mNotifyManager.notify(0, mBuilder.build());
    }

    public void installApk(Context context, File apkFile) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(getFileUri(context,apkFile),
                "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(installIntent);
    }

    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {//7.0及以上版本
            return FileProvider.getUriForFile(context,
                    context.getPackageName()+ ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    class UpdateDialog {
        private Context context;
        private AlertDialog dialog; //悬浮框

        public UpdateDialog(Context context) {
            this.context = context;
        }

        /**
         * 显示下载对话框
         */
        private void show() {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_recommend_update, null);
            dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            Window dialogWindow = dialog.getWindow();
            dialogWindow.setContentView(view);

            TextView titleTv = view.findViewById(R.id.update_title_tv);
            TextView sureTv = view.findViewById(R.id.update_sure_tv);
            TextView waitTv = view.findViewById(R.id.update_wait_tv);

            waitTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            sureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (call == null || call.isCanceled()) {
                        download(context, url);
                    }
                }
            });

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
        }

    }

}
