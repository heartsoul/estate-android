package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.qualityManage.bean.ModelComponent;
import com.glodon.bim.business.qualityManage.bean.ModelElementHistory;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.contract.RelevantModelContract;
import com.glodon.bim.business.qualityManage.presenter.RelevantModelPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.glodon.bim.R.id.webView;

/**
 * 描述：关联模型-模型展示
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class RelevantModelActivity extends BaseActivity implements View.OnClickListener, RelevantModelContract.View {

    private View mStatusView;
    private RelativeLayout mBackView;
    private RelativeLayout mFinishView;
    private WebView mWebview;
    private String mFileName = "";
    private String mFileId = "";//模型id

    private RelevantBluePrintAndModelDialog mRepairDialog;
    private RelevantBluePrintAndModelDialog mReviewDialog;

    private RelevantModelContract.Presenter mPresenter;

    private ModelListBeanItem mModelSelectInfo;//编辑时有过这个item
    private ModelComponent component;//选中的构件
    private int type = 0;//0新建检查单 1检查单编辑状态 2详情查看  3模型模式
    private boolean show = false;//true  不相应长按事件  false相应长按事件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_relevant_model_activity);
        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();

    }

    private void initView() {
        mStatusView = findViewById(R.id.relevant_model_statusview);
        mBackView = (RelativeLayout) findViewById(R.id.relevant_model_back);
        mFinishView = (RelativeLayout) findViewById(R.id.relevant_model_finish);
        mWebview = (WebView) findViewById(R.id.relevant_model_webview);
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
        mWebview.addJavascriptInterface(new ModelEvent(), "modelEvent");
        setting.setDomStorageEnabled(false);

        // 暂时先去掉（在HuaWeiP6上显示异常）
        // this.setLayerType(WebView.LAYER_TYPE_HARDWARE, new Paint());
//        setting.setAppCacheMaxSize(1024 * 1024 * 8);
//        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(false);
        setting.setDatabaseEnabled(false);
        setting.setUseWideViewPort(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        // setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        // setting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        setting.setLoadWithOverviewMode(true);
    }


    @Override
    public void sendBasicInfo(String token) {
        String url = AppConfig.BASE_URL_BLUEPRINT_TOKEN +token+"&show="+show;
        LogUtil.e("url=" + url);
        mWebview.loadUrl(url);
    }


    // 点击构件的回调
    class ModelEvent {

        //模型加载完毕后的回调
        @JavascriptInterface
        public void loadDotsData() {
            LogUtil.e("loadDotsData");
            pageFinished();
        }

        //token失效的情况
        @JavascriptInterface
        public void invalidateToken() {
            LogUtil.e("invalidateToken");
            showTokenError();
        }

        //取消选中构件
        @JavascriptInterface
        public void cancelPosition() {
            LogUtil.e("cancelPosition");
            component = null;
        }

        //点击构件返回信息
        @JavascriptInterface
        public void getPosition(final String json) {
            LogUtil.e("getPosition json="+json);
            component = new GsonBuilder().create().fromJson(json, ModelComponent.class);
        }

        //点击圆点 返回信息
        @JavascriptInterface
        public void getPositionInfo(final String json) {
            LogUtil.e("getPositionInfo json="+json);
            final ModelElementHistory dot = new GsonBuilder().create().fromJson(json, ModelElementHistory.class);
            if (dot != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//          {"全部", "待提交",  "待整改",       "待复查",      "已检查",      "已复查",    "已延迟",  "已验收"};
//          {"",     "staged", "unrectified",  "unreviewed",  "inspected",  "reviewed",  "delayed","accepted"};
                        switch (dot.qcState) {
                            case CommonConfig.QC_STATE_UNRECTIFIED://"待整改"
                                if (AuthorityManager.isCreateRepair() && AuthorityManager.isMe(dot.responsibleUserId)) {
                                    showRepairDialog(dot);
                                }
                                break;
                            case CommonConfig.QC_STATE_UNREVIEWED://"待复查"
                                if (AuthorityManager.isCreateReview() && AuthorityManager.isMe(dot.inspectionUserId)) {
                                    showReviewDialog(dot);
                                }
                                break;
                        }
                    }
                });
            }
        }
    }


    private void setListener() {
        mBackView.setOnClickListener(this);
        mFinishView.setOnClickListener(this);

    }

    private void initData() {
        //入口类型
        type = getIntent().getIntExtra(CommonConfig.RELEVANT_TYPE,0);
        mModelSelectInfo = (ModelListBeanItem) getIntent().getSerializableExtra(CommonConfig.MODEL_SELECT_INFO);
        mFileName = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_NAME);
        mFileId = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_ID);
        handleType();
        //初始化底部弹出框
        mRepairDialog = new RelevantBluePrintAndModelDialog(this);
        mReviewDialog = new RelevantBluePrintAndModelDialog(this);

        //获取数据
        mPresenter = new RelevantModelPresenter(this);
        mPresenter.initData(getIntent());
    }

    //不同的入口处理数据不同
    private void handleType(){
        //0新建检查单 1检查单编辑状态 2详情查看  3模型模式
        switch (type){
            case 0:
                show = false;
                break;
            case 1:
                show = false;
                break;
            case 2:
                show = true;
                mFinishView.setVisibility(View.GONE);
                break;
            case 3:
                show = false;
                //判断是否有权限新建检查单
                if (!AuthorityManager.isShowCreateButton()) {
                    //无权限
                    show = true;//不响应点击事件
                    mFinishView.setVisibility(View.GONE);
                }
                break;
        }
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.relevant_model_back://返回键
                mActivity.finish();
                break;
            case R.id.relevant_model_finish://+号
                //0新建检查单 1检查单编辑状态 2详情查看  3模型模式
                switch (type){
                    case 0:
                        if(checkComponent()){
                            backData();
                        }
                        break;
                    case 1:
                        if(checkComponent()){
                            backData();
                        }
                        break;
                    case 2:

                        break;
                    case 3:
                        if(checkComponent()) {
                            //跳转到检查单创建页
                            Intent intent = new Intent(mActivity, CreateCheckListActivity.class);
                            mModelSelectInfo.component = component;
                            intent.putExtra(CommonConfig.RELEVANT_MODEL, mModelSelectInfo);
                            startActivity(intent);
                            finish();
                        }
                        break;
                }

                break;
        }
    }

    //检测是否选择了构件
    private boolean checkComponent(){
        if(component==null||"undefined".equals(component.elementId)) {
            SaveDeleteDialog mHintDialog = new SaveDeleteDialog(getActivity());
            mHintDialog.getModelHintDialog("您还未选择构件!");
            mHintDialog.show();
            return false;
        }
        return true;
    }

    private void backData(){
        Intent data = new Intent();
        mModelSelectInfo.component = component;
        data.putExtra(CommonConfig.MODEL_SELECT_INFO, mModelSelectInfo);
        setResult(RESULT_OK, data);
        finish();
    }



    //设定选中的构件  单个和多个
    private void showSelectedComponent(List<String> list){
        String param = new GsonBuilder().create().toJson(list);
        mWebview.loadUrl("javascript:showSelectedComponent('" + param + "')");
    }
    //设置多个点
    @Override
    public void showModelHistory(List<ModelElementHistory> list) {
//        if(list==null){
//            String str = " {\n" +
//                    "        \"drawingPositionX\": -9795.7915,\n" +
//                    "        \"drawingPositionY\": -1652.2905,\n" +
//                    "        \"drawingPositionZ\": 2145,\n" +
//                    "        \"elementId\": \"332707\",\n" +
//                    "        \"elementName\": \"1200 x 2100mm\",\n" +
//                    "        \"gdocFileId\": \"bfaee9baba154c3b9a8343dc1ff48845\",\n" +
//                    "        \"inspectionId\": 5200069,\n" +
//                    "        \"inspectionUserId\": 5200003,\n" +
//                    "        \"qcState\": \"unrectified\",\n" +
//                    "        \"rectificationId\": 0,\n" +
//                    "        \"responsibleUserId\": 5200013,\n" +
//                    "        \"reviewId\": 0\n" +
//                    "    }";
//            String str2 = "{\n" +
//                    "        \"drawingPositionX\": -1638.2932,\n" +
//                    "        \"drawingPositionY\": 6979.591,\n" +
//                    "        \"drawingPositionZ\": 2723.249,\n" +
//                    "        \"elementId\": \"333105\",\n" +
//                    "        \"elementName\": \"百叶窗\",\n" +
//                    "        \"gdocFileId\": \"bfaee9baba154c3b9a8343dc1ff48845\",\n" +
//                    "        \"inspectionId\": 5200071,\n" +
//                    "        \"inspectionUserId\": 5200003,\n" +
//                    "        \"qcState\": \"unrectified\",\n" +
//                    "        \"rectificationId\": 0,\n" +
//                    "        \"responsibleUserId\": 5200013,\n" +
//                    "        \"reviewId\": 0\n" +
//                    "    }";
//            String str3 = "{\n" +
//                    "        \"drawingPositionX\": -3738.2915000000003,\n" +
//                    "        \"drawingPositionY\": 7147.709,\n" +
//                    "        \"drawingPositionZ\": 2760,\n" +
//                    "        \"elementId\": \"313507\",\n" +
//                    "        \"elementName\": \"墙面\",\n" +
//                    "        \"gdocFileId\": \"bfaee9baba154c3b9a8343dc1ff48845\",\n" +
//                    "        \"inspectionId\": 5200070,\n" +
//                    "        \"inspectionUserId\": 5200003,\n" +
//                    "        \"qcState\": \"unrectified\",\n" +
//                    "        \"rectificationId\": 0,\n" +
//                    "        \"responsibleUserId\": 5200013,\n" +
//                    "        \"reviewId\": 0\n" +
//                    "    }";
//            list =  new ArrayList<>();
//            list.add(new GsonBuilder().create().fromJson(str,ModelElementHistory.class));
//            list.add(new GsonBuilder().create().fromJson(str2,ModelElementHistory.class));
//            list.add(new GsonBuilder().create().fromJson(str3,ModelElementHistory.class));
//        }
        //设置多点
        String param = new GsonBuilder().create().toJson(list);
        LogUtil.e("设置多点 params="+param);
        mWebview.loadUrl("javascript:loadCircleItems('" + param + "')");
//        mWebview.loadUrl("javascript:loadCircleItems()");

        //设置多个构件选中
//        List<String> dotList = new ArrayList<>();
//        for(ModelElementHistory history:list){
//            dotList.add(history.elementId);
//        }
//        showSelectedComponent(dotList);
    }

    @Override
    public void showTokenError() {
        ToastManager.show("抱歉，您目前没有查看此模型的权限，请联系系统管理员。");
    }


    //新建整改单的弹出框
    private void showRepairDialog(final ModelElementHistory dot) {
        mRepairDialog.getRepairDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建整改单
                Intent intent = new Intent(mActivity, CreateReviewActivity.class);
                intent.putExtra(CommonConfig.CREATE_TYPE, CommonConfig.CREATE_TYPE_REPAIR);
                intent.putExtra(CommonConfig.SHOW_PHOTO, true);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, dot.inspectionId);
                startActivity(intent);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看详情
                Intent intent = new Intent(mActivity, QualityCheckListDetailActivity.class);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, dot.inspectionId);
                startActivity(intent);
            }
        }).show();
    }

    //新建复查单的弹出框
    private void showReviewDialog(final ModelElementHistory dot) {
        mReviewDialog.getReviewDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建复查单
                Intent intent = new Intent(mActivity, CreateReviewActivity.class);
                intent.putExtra(CommonConfig.CREATE_TYPE, CommonConfig.CREATE_TYPE_REVIEW);
                intent.putExtra(CommonConfig.SHOW_PHOTO, true);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, dot.inspectionId);
                startActivity(intent);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看详情
                Intent intent = new Intent(mActivity, QualityCheckListDetailActivity.class);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, dot.inspectionId);
                startActivity(intent);
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

     private void pageFinished(){
         //0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
         LogUtil.e("pageFinish type="+type);
         switch (type){
             case 0:

                 break;
             case 1:
                 if(mModelSelectInfo!=null){
                     component = mModelSelectInfo.component;
                     if(component!=null) {
                         List<String> list = new ArrayList<>();
                         list.add(mModelSelectInfo.component.elementId);
                         showSelectedComponent(list);
                     }
                 }
                 break;
             case 2:
                 if(mModelSelectInfo!=null){
                     component = mModelSelectInfo.component;
                     if(component!=null) {
                         List<String> list = new ArrayList<>();
                         list.add(mModelSelectInfo.component.elementId);
                         showSelectedComponent(list);
                     }
                 }
                 break;
             case 3:
                 mPresenter.getElements();
//                    showModelHistory(null);
                 break;
         }
         dismissLoadingDialog();
     }

    class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            pageFinished();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebview!=null){
            LogUtil.e("ondestroy");
            //清空所有Cookie
            CookieSyncManager.createInstance(BaseApplication.getInstance());  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.
            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

            mWebview.setWebChromeClient(null);
            mWebview.setWebViewClient(null);
            mWebview.getSettings().setJavaScriptEnabled(false);
            mWebview.clearCache(true);
        }
    }
}
