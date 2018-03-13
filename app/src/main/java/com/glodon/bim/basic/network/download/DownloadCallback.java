package com.glodon.bim.basic.network.download;

import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 下载回调
 * Created by chenwj-a on 2017/12/6.
 */

public class DownloadCallback implements Callback {

    private Handler mHandler;
    private DownloadResponseHandler mDownloadResponseHandler;
    private String mFileDir;
    private String mFilename;

    public DownloadCallback(Handler handler, DownloadResponseHandler downloadResponseHandler,
                            String filedir, String filename) {
        mHandler = handler;
        mDownloadResponseHandler = downloadResponseHandler;
        mFileDir = filedir;
        mFilename = filename;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDownloadResponseHandler.onFailure(e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            File file = null;
            try {
                file = saveFile(response, mFileDir, mFilename);
            } catch (final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFailure("onResponse saveFile fail." + e.toString());
                    }
                });
            }

            final File newFile = file;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadResponseHandler.onFinish(newFile);
                }
            });
        } else {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadResponseHandler.onFailure("fail status=" + response.code());
                }
            });
        }
    }

    //保存文件
    private File saveFile(Response response, String filedir, String filename) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filedir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }
}
