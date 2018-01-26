package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.qualityManage.bean.BluePrintDotItem;
import com.glodon.bim.business.qualityManage.bean.BluePrintPosition;
import com.glodon.bim.business.qualityManage.bean.BluePrintPositionItem;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;
import com.glodon.bim.business.qualityManage.presenter.RelevantBluePrintPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
    private RelevantBluePrintAndModelDialog mReviewDialog, mQualityDetailDialog;

    private RelevantBluePrintContract.Presenter mPresenter;

    private String drawingPositionX;//位置的x信息
    private String drawingPositionY;//位置的y信息

    private int type = 0;//0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
    private boolean show = false;//true  不相应长按事件  false相应长按事件

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
        String url = AppConfig.BASE_URL_BLUEPRINT_TOKEN + token + "&show=" + show;
        LogUtil.e("url=" + url);
        mWebview.loadUrl(url);
    }

    @Override
    public void setDotsData(List<BluePrintDotItem> list) {
        String dots = new GsonBuilder().create().toJson(list);
        String url = "javascript:loadCircleItems('" + dots + "')";
        LogUtil.e("order dots=" + url);
        mWebview.loadUrl(url);
    }

    private void setListener() {
        mBackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.relevant_blueprint_back://返回键
                mActivity.finish();
                break;
        }
    }

    private void initData() {
        //类型
        type = getIntent().getIntExtra(CommonConfig.RELEVANT_TYPE, 0);
        //初始的位置信息
        drawingPositionX = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_POSITION_X);
        drawingPositionY = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_POSITION_Y);
        handleType();
        //初始化底部弹出框
        mRepairDialog = new RelevantBluePrintAndModelDialog(this);
        mReviewDialog = new RelevantBluePrintAndModelDialog(this);
        mQualityDetailDialog = new RelevantBluePrintAndModelDialog(this);
        //title名字
        mFileName = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_NAME);
        mFileId = getIntent().getStringExtra(CommonConfig.BLUE_PRINT_FILE_ID);
        if (!TextUtils.isEmpty(mFileName)) {
            mTitleView.setText(Html.fromHtml(mFileName));
        }
        //隐藏提示
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTrangleTextView.setVisibility(View.GONE);
                mTrangleView.setVisibility(View.GONE);
            }
        }, 4000);

        //获取数据
        mPresenter = new RelevantBluePrintPresenter(this);
        mPresenter.initData(getIntent());
    }

    //不同类型的处理
    private void handleType() {
        //0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
        switch (type) {
            case 0:
                show = false;
                break;
            case 1:
                show = false;
                break;
            case 2:
                mFinishView.setVisibility(View.GONE);
                mTrangleView.setVisibility(View.GONE);
                mTrangleTextView.setVisibility(View.GONE);
                show = true;
                break;
            case 3:
                show = false;
                //判断是否有权限新建检查单
                if (!AuthorityManager.isShowCreateButton()) {
                    //无权限
                    show = true;//不响应点击事件
                    mFinishView.setVisibility(View.GONE);
                    mTrangleView.setVisibility(View.GONE);
                    mFinishView.setVisibility(View.GONE);
                }
                break;
        }
    }

    //不同type的返回处理
    private void setResultByType() {
        //0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
        switch (type) {
            case 0:
            case 1://将值返回
                Intent data = new Intent();
                BlueprintListBeanItem item = new BlueprintListBeanItem();
                item.name = mFileName;
                item.fileId = mFileId;
                item.drawingPositionX = drawingPositionX;
                item.drawingPositionY = drawingPositionY;
                data.putExtra(CommonConfig.MODULE_LIST_NAME, item);
                setResult(RESULT_OK, data);
                finish();
                break;
            case 2://无

                break;
            case 3://跳转到新建检查单
                Intent intent = new Intent(mActivity, CreateCheckListActivity.class);
                intent.putExtra(CommonConfig.DRAWINGGDOCFILEID, mFileId);
                intent.putExtra(CommonConfig.DRAWINGNAME, mFileName);
                intent.putExtra(CommonConfig.DRAWINGPOSITIONX, drawingPositionX);
                intent.putExtra(CommonConfig.DRAWINGPOSITIONY, drawingPositionY);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
        }
    }

    private void showFinish() {
        mCancelView.setVisibility(View.VISIBLE);
        mBackView.setVisibility(View.GONE);
        mFinishView.setText("完成");
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResultByType();
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


    private void hideFinish() {
        mCancelView.setVisibility(View.GONE);
        mBackView.setVisibility(View.VISIBLE);
        mFinishView.setText("长按新建");
        mFinishView.setOnClickListener(null);
    }

    //新建整改单的弹出框
    private void showRepairDialog(final BluePrintDotItem dot, boolean create, boolean browser) {
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
        }, create, browser).show();
    }

    //新建复查单的弹出框
    private void showReviewDialog(final BluePrintDotItem dot, boolean create, boolean browser) {
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
        }, create, browser).show();
    }

    //查看质量详情
    private void showDetailDialog(final BluePrintDotItem dot, boolean browser) {
        mQualityDetailDialog.getQualityDetailDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看详情
                Intent intent = new Intent(mActivity, QualityCheckListDetailActivity.class);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, dot.inspectionId);
                startActivity(intent);
            }
        }, browser).show();
    }


    @Override
    public void showTokenError() {
        ToastManager.show("抱歉，您目前没有查看此图纸的权限，请联系系统管理员。");
    }

    class ModelEvent {

        //token失效的情况
        @JavascriptInterface
        public void invalidateToken() {
            LogUtil.e("invalidateToken");
            showTokenError();
        }

        //长按图纸 返回点的信息
        @JavascriptInterface
        public void getPosition(final String json) {
            LogUtil.e("getPosition  json=" + json);
            BluePrintPosition position = new GsonBuilder().create().fromJson(json, BluePrintPosition.class);
            if (position != null) {
                drawingPositionX = position.x;
                drawingPositionY = position.y;
            }
            LogUtil.e("x=" + drawingPositionX + " y=" + drawingPositionY);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showFinish();
                }
            });

        }

        //点击圆点 返回信息
        @JavascriptInterface
        public void getPositionInfo(final String json) {
            LogUtil.e("getPositionInfo  json=" + json);
            final BluePrintDotItem dot = new GsonBuilder().create().fromJson(json, BluePrintDotItem.class);
            if (dot != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//          {"全部", "待提交",  "待整改",       "待复查",      "已检查",      "已复查",    "已延迟",  "已验收"};
//          {"",     "staged", "unrectified",  "unreviewed",  "inspected",  "reviewed",  "delayed","accepted"};
                        switch (dot.qcState) {
                            case CommonConfig.QC_STATE_UNRECTIFIED://"待整改"
                            case CommonConfig.QC_STATE_DELAYED://"已延迟"
                                showRepairDialog(dot, AuthorityManager.isCreateRepair() && AuthorityManager.isMe(dot.responsibleUserId), AuthorityManager.isQualityBrowser());
                                break;
                            case CommonConfig.QC_STATE_UNREVIEWED://"待复查"
                                showReviewDialog(dot, AuthorityManager.isCreateReview() && AuthorityManager.isMe(dot.inspectionUserId), AuthorityManager.isQualityBrowser());
                                break;
                            case CommonConfig.QC_STATE_INSPECTED://"已检查"
                            case CommonConfig.QC_STATE_REVIEWED://"已复查"
                            case CommonConfig.QC_STATE_ACCEPTED://"已验收"
                                showDetailDialog(dot, AuthorityManager.isQualityBrowser());
                                break;
                        }
                    }
                });
            }


        }
    }


    //消除图钉
    private void removePosition(String param) {
        mWebview.loadUrl("javascript:removeDrawableItem('" + param + "')");
    }

    //设定图钉
    private void setPosition() {
        BluePrintPositionItem info = new BluePrintPositionItem();
        LogUtil.e("setposition=" + drawingPositionX + "  " + drawingPositionY);
        if (!TextUtils.isEmpty(drawingPositionX) && !TextUtils.isEmpty(drawingPositionY)) {
            info.x = Double.parseDouble(drawingPositionX);
            info.y = Double.parseDouble(drawingPositionY);
            info.z = 0;
            List<BluePrintPositionItem> list = new ArrayList<>();
            list.add(info);
            String param = new GsonBuilder().create().toJson(list);
            String order = "javascript:loadPinItems('" + param + "')";
            LogUtil.e("order=" + order);
            mWebview.loadUrl(order);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case 0:

                            break;
                        case 1:
                            showFinish();
                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                    }

                }
            });
        }
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


    class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            setInitData();
            showLoadingDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
            LogUtil.e("pageFinish type=" + type);
            switch (type) {
                case 0:
//                    sendDotsData();
                    break;
                case 1:
                    setPosition();
                    break;
                case 2:
                    setPosition();
                    break;
                case 3:
                    mPresenter.getBluePrintDots();
                    break;
            }
            dismissLoadingDialog();
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
        if (mWebview != null) {
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
