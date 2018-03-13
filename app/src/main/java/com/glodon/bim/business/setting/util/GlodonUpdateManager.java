package com.glodon.bim.business.setting.util;

import android.app.NotificationManager;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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

    private GlodonUpdateManager(){

    }

    public static GlodonUpdateManager getInstance() {
        if (sInstance == null) {
            sInstance = new GlodonUpdateManager();
        }
        return sInstance;
    }

    public void download(String url){
        String path = Environment.getExternalStorageDirectory()+"/bim/download/";
        String fileName = "test.apk";
        call = GlodonDownloadManager.getInstance().download(url, path, fileName, new DownloadResponseHandler() {
            @Override
            public void onFinish(File download_file) {
                Log.i("SettingActivity", "download finish "+download_file.getName());
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                int progress = (int) (currentBytes * 100 / totalBytes);
                Log.i("SettingActivity", "download progress: "+progress);//下载通知栏
            }

            @Override
            public void onFailure(String error_msg) {
                Log.i("SettingActivity", "download fail "+error_msg);
            }
        });


    }


}
