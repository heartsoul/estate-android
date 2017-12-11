package com.glodon.bim.business.qualityManage.view;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.qualityManage.bean.BluePrintBasicInfo;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：关联图纸-图纸展示
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class RelevantBluePrintActivity extends BaseActivity implements View.OnClickListener {

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mCancelView,mTitleView,mFinishView;
    private WebView mWebview;
    private ImageView mTrangleView;
    private TextView mTrangleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_relevant_blueprint_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.relevant_blueprint_statusview);
        mBackView = (RelativeLayout) findViewById(R.id.relevant_blueprint_back);
        mCancelView = (TextView) findViewById(R.id.relevant_blueprint_cancel);
        mTitleView = (TextView) findViewById(R.id.relevant_blueprint_title);
        mFinishView = (TextView) findViewById(R.id.relevant_blueprint_finish);
        mWebview = (WebView) findViewById(R.id.relevant_blueprint_webview);
        mTrangleView = (ImageView) findViewById(R.id.relevant_blueprint_trangle);
        mTrangleTextView = (TextView) findViewById(R.id.relevant_blueprint_trangle_content);

        initWebview();
    }

    private void initWebview(){
        mWebview.setWebChromeClient(new WebChromeClient());
        /**
         * 依旧当前webview加载新的html
         */
        mWebview.setWebViewClient(new CustomWebViewClient());
        // 解决html中js跨域问题
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = mWebview.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(mWebview.getSettings(), true);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        // 一般设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebview.setWebContentsDebuggingEnabled(true);
        }

        WebSettings setting = mWebview.getSettings();
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setPluginState(WebSettings.PluginState.ON);
        setting.setJavaScriptEnabled(true);
        mWebview.addJavascriptInterface(new BasicInfo(), "BasicInfo");
        setting.setDomStorageEnabled(true);
        // 暂时先去掉（在HuaWeiP6上显示异常）
        // this.setLayerType(WebView.LAYER_TYPE_HARDWARE, new Paint());
        setting.setAppCacheMaxSize(1024 * 1024 * 8);
//        String appCachePath = TNBToonBrowserApplication.getInstance().getApplicationContext().getCacheDir()
//                .getAbsolutePath();
//        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        // setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        // setting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        setting.setLoadWithOverviewMode(true);
    }

    class BasicInfo {

        @JavascriptInterface
        public void getIpPort(String str) {
            ipPort(str);
        }
    }

    /**
     * 告诉H5ip和port
     */
    private void ipPort(final String str) {
//        final String ip = TNBStartMwapUtil.getCurrentIP(getContext());
//        final int port = TNBStartMwapUtil.getCurrentPort(getContext());
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("ip", ip + ":" + port);
//        params.put("ip", ip + ":" + TNBConfig.PORT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                TNBWebView.this.sendParamsToHtml(TNBMethodConfig.IP_PORT, params);
                ToastManager.show(str);
            }
        });
    }

    private void setListener() {

    }

    private void initData() {
        mWebview.loadUrl(getUrl());
    }

    private String getUrl(){
        BluePrintBasicInfo info = new BluePrintBasicInfo();
        info.projectId = "projectId1";
        info.projectVersionId = "projectVersionId1";
        info.fileId = "fileId1";
        return AppConfig.BASE_URL_BLUEPRINT+"?param="+new GsonBuilder().create().toJson(info);
    }

    //传递基本信息给h5  我的id  项目id   图纸id

    //传递点的信息给h5
    public void sendDotsData(){
        BluePrintBasicInfo info = new BluePrintBasicInfo();
        info.projectId = "projectId2";
        info.projectVersionId = "projectVersionId2";
        info.fileId = "fileId2";
        sendDataToHtml("loadInitData",new GsonBuilder().create().toJson(info));
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sms_code_back://返回键
                mActivity.finish();
                break;
        }
    }

    /**
     * 创建返回的事件
     *
     * @param eventName
     * @param json
     * @return
     */
    private static String createReturnUrl(String eventName, String json) {
        String temp = "javascript:{ var e = document.createEvent('Event');" + "e.data=" + json + ";" + "e.initEvent('"
                + eventName + "',false,true);" + "document.dispatchEvent(e);}";
        LogUtil.e("向H5传递参数=======" + temp);
        return temp;
    }

    public void sendDataToHtml(String callbackMethodName, String json) {
//        TNBMethodConfig.returnToHtml(this, eventName, params);
//        this.loadUrl("javascript:"+callbackMethodName+"(\""+json+"\")");
        LogUtil.e("json=============" + json);

        mWebview.loadUrl("javascript:" + callbackMethodName + "('" + json + "')");
    }

    class CustomWebViewClient extends  WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            sendDotsData();
        }
    }
}
