package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
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
import com.glodon.bim.business.qualityManage.bean.BluePrintPosition;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
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
    private String mFileName = "";//图纸名
    private String mFileId = "";//图纸id

    private RelevantBluePrintAndModelDialog mRepairDialog;
    private RelevantBluePrintAndModelDialog mReviewDialog;

    private RelevantBluePrintContract.Presenter mPresenter;

    private String drawingPositionX;//位置的x信息
    private String drawingPositionY;//位置的y信息

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
        mWebview.addJavascriptInterface(new ModelEvent(), "modelEvent");
        setting.setDomStorageEnabled(false);
        // 暂时先去掉（在HuaWeiP6上显示异常）
        // this.setLayerType(WebView.LAYER_TYPE_HARDWARE, new Paint());
//        setting.setAppCacheMaxSize(1024 * 1024 * 8);
//        String appCachePath = TNBToonBrowserApplication.getInstance().getApplicationContext().getCacheDir()
//                .getAbsolutePath();
//        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(false);
        setting.setDatabaseEnabled(false);
        setting.setUseWideViewPort(true);
//        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        // setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        // setting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        setting.setLoadWithOverviewMode(true);
    }


    @Override
    public void sendBasicInfo(String token) {
        String url = AppConfig.BASE_URL_BLUEPRINT_TOKEN+token;
        LogUtil.e("url="+url);
        mWebview.loadUrl(url);
    }

//    window.modelEvent.getPosition
    class ModelEvent {

        @JavascriptInterface
        public void getPosition(final String json) {
            LogUtil.e("json="+json);
            BluePrintPosition position = new GsonBuilder().create().fromJson(json,BluePrintPosition.class);
            if(position!=null){
                drawingPositionX = position.x;
                drawingPositionY = position.y;
            }
            LogUtil.e("x="+drawingPositionX+" y="+drawingPositionY);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showFinish();
                }
            });

        }
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
        mBackView.setOnClickListener(this);
    }

    private void initData() {
        //初始的位置信息
        drawingPositionX = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_POSITION_X);
        drawingPositionY = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_POSITION_Y);
        //初始化底部弹出框
        mRepairDialog = new RelevantBluePrintAndModelDialog(this);
        mReviewDialog = new RelevantBluePrintAndModelDialog(this);
        //title名字
        mFileName = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_NAME);
        mFileId = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_ID);
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
                mActivity.finish();
//                if(isshow){
//                    showRepairDialog();
//                }else {
//                    showReviewDialog();
//                }
//                isshow = !isshow;
                break;
        }
    }

    private void showFinish(){
        mCancelView.setVisibility(View.VISIBLE);
        mBackView.setVisibility(View.GONE);
        mFinishView.setText("完成");
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                BlueprintListBeanItem item = new BlueprintListBeanItem();
                item.name = mFileName;
                item.fileId = mFileId;
                item.drawingPositionX = drawingPositionX;
                item.drawingPositionY = drawingPositionY;
                data.putExtra(CommonConfig.MODULE_LIST_NAME,item);
                setResult(RESULT_OK,data);
                finish();
            }
        });
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //消除图钉
                removePosition("aaa");
                //展示返回按钮
                hideFinish();
            }
        });
    }

    //消除图钉
    private void removePosition(String param) {
        mWebview.loadUrl("javascript:removeDrawableItem('" + param + "')");
    }

    private void hideFinish(){
        mCancelView.setVisibility(View.GONE);
        mBackView.setVisibility(View.VISIBLE);
        mFinishView.setText("长按新建");
        mFinishView.setOnClickListener(null);
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
