package com.glodon.bim.test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.glodon.bim.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;

import java.util.Map;

public class TestShareActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private Button btnShowPanal;
    private UMShareListener listener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Toast.makeText(activity,"start",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(activity,"成功了",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(activity,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(activity,"取消了",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_share);
        activity = this;
        btnShowPanal = findViewById(R.id.btn_showpanael);

        setLisener();
        requestPermission();
    }

    private void setLisener() {
        btnShowPanal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_showpanael:
                showSharePannel();
                break;

        }
    }

    //分享文字
    private void ShareText() {
        new ShareAction(activity).withText("测试")
                .setPlatform(SHARE_MEDIA.SINA)
                .setCallback(listener).share();
    }




    //分享图文
    private void ShareImage(String image, String thumb) {
        UMImage pic = new UMImage(activity, image);
        pic.setThumb(new UMImage(activity, thumb));
        new ShareAction(activity).withMedia(pic).setPlatform(SHARE_MEDIA.SINA).setCallback(listener).withText("测试").share();
    }

    //分享链接
    private void ShareWeb(String thumb_img) {
        UMImage thumb = new UMImage(activity, thumb_img);
        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setThumb(thumb);
        web.setDescription("测试描述");
        web.setTitle("测试title");
        new ShareAction(activity).withMedia(web).setPlatform(SHARE_MEDIA.SINA).setCallback(listener).share();
    }

    //分享音乐
    private void ShareMusic(String thumb_img) {
        UMImage thumb = new UMImage(activity, thumb_img);
        UMusic music = new UMusic("http://www.baidu.com/33.mp3");
        music.setThumb(thumb);
        music.setDescription("测试描述");
        music.setTitle("测试title");
        new ShareAction(activity).withMedia(music).setPlatform(SHARE_MEDIA.SINA).setCallback(listener).share();
    }

    //分享视频
    private void ShareVideo(String thumb_img) {
        UMImage thumb = new UMImage(activity, thumb_img);
        UMVideo video = new UMVideo("http://www.baidu.com/33.mp4");
        video.setThumb(thumb);
        video.setDescription("测试描述");
        video.setTitle("测试title");
        new ShareAction(activity).withMedia(video).setPlatform(SHARE_MEDIA.SINA).setCallback(listener).share();
    }

    //权限申请
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    //授权代码
    private void authorize() {

        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }


    private void showSharePannel(){
//        new ShareAction(activity)
//                .withText("hello")
//                .setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA,SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL)
//                .setCallback(listener)
//                .open();
        isHasClient(SHARE_MEDIA.WEIXIN);
        isHasClient(SHARE_MEDIA.WEIXIN_CIRCLE);
        isHasClient(SHARE_MEDIA.QQ);
        isHasClient(SHARE_MEDIA.QZONE);
        isHasClient(SHARE_MEDIA.SINA);
        isHasClient(SHARE_MEDIA.SMS);
        isHasClient(SHARE_MEDIA.EMAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    /**
     * 判断是否安装了客户端
     */
    private  boolean isHasClient(SHARE_MEDIA client){
//        return UMShareAPI.get(activity).isInstall(this, SHARE_MEDIA.WEIXIN);
        boolean result = UMShareAPI.get(activity).isInstall(this, client);
        System.out.println(client.toString()+ " result= "+result);
        return result;
    }
}
