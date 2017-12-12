package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.qualityManage.bean.BluePrintBasicInfo;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;
import com.glodon.bim.business.qualityManage.presenter.RelevantBluePrintPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * 描述：关联图纸-图纸展示
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class RelevantBluePrintActivity extends BaseActivity implements View.OnClickListener, RelevantBluePrintContract.View {

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mCancelView, mTitleView, mFinishView;
    private WebView mWebview;
    private ImageView mTrangleView;
    private TextView mTrangleTextView;
    private String mFileName = "";

    private RelevantBluePrintAndModelDialog mRepairDialog;
    private RelevantBluePrintAndModelDialog mReviewDialog;

    private RelevantBluePrintContract.Presenter mPresenter;

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

    private void initWebview() {
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


    @Override
    public void sendBasicInfo(String token) {
        // param = {
        //     "fileId":"de3a033fa5a046eeb11ae6994d3f124c",
        //     "projectId": '5211517',
        //     "projectVersionId": '0e44a8f4f2fc4b7eb6c097aa361f342b'
        // }
//        String url = getUrl(5211517, "0e44a8f4f2fc4b7eb6c097aa361f342b", "de3a033fa5a046eeb11ae6994d3f124c");
//        String url = getUrl(mProjectId, mProjectVersionId, mFileId);  http://47.95.204.243/app.html
//        String url = "http://47.95.204.243/app.html?param={\"fileId\":\"dff5aed79cf5407687b91ee42b2ebb91\",\"projectId\":\"5200103\",\"projectVersionId\":\"da563a7890e24d90b8adb23a0883f270\"}";
//        String url = "http://47.95.204.243/app.html?param=a585d8bf293340c3beac83217ab8f924";
        String url = AppConfig.BASE_URL_BLUEPRINT_TOKEN+token;
        LogUtil.e("url="+url);
        mWebview.loadUrl(url);
//        try {
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

//    window.modelEvent.getPosition


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
        mBackView.setOnClickListener(this);
    }

    private void initData() {
        //初始化底部弹出框
        mRepairDialog = new RelevantBluePrintAndModelDialog(this);
        mReviewDialog = new RelevantBluePrintAndModelDialog(this);
        //title名字
        mFileName = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_NAME);
        mTitleView.setText(mFileName);
        //隐藏提示
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTrangleTextView.setVisibility(View.GONE);
                mTrangleView.setVisibility(View.GONE);
            }
        },4000);

        //获取数据
        mPresenter = new RelevantBluePrintPresenter(this);
        mPresenter.initData(getIntent());

    }

//    pr

    //传递基本信息给h5  我的id  项目id   图纸id

    //传递点的信息给h5
    public void sendDotsData() {
        BluePrintBasicInfo info = new BluePrintBasicInfo();
        info.projectId = "projectId2";
        info.projectVersionId = "projectVersionId2";
        info.fileId = "fileId2";
//        sendDataToHtml("loadInitData", new GsonBuilder().create().toJson(info));
    }

    private boolean isshow = true;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.relevant_blueprint_back://返回键
//                mActivity.finish();
                if(isshow){
                    showRepairDialog();
                }else {
                    showReviewDialog();
                }
                isshow = !isshow;
                break;
        }
    }

    //新建整改单的弹出框
   private void showRepairDialog(){
       mRepairDialog.getRepairDialog(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       }, new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       }).show();
   }

   //新建复查单的弹出框
    private void showReviewDialog(){
        mReviewDialog.getReviewDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).show();
    }

    /**
     * 创建返回的事件
     */
    private static String createReturnUrl(String eventName, String json) {
        String temp = "javascript:{ var e = document.createEvent('Event');" + "e.data=" + json + ";" + "e.initEvent('"
                + eventName + "',false,true);" + "document.dispatchEvent(e);}";
        LogUtil.e("向H5传递参数=======" + temp);
        return temp;
    }

    public void sendDataToHtml(String callbackMethodName, String json) {
        mWebview.loadUrl("javascript:" + callbackMethodName + "('" + json + "')");
    }

    class CustomWebViewClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            sendDotsData();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }


    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }
}
