package com.glodon.bim.business.main.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.main.presenter.ChooseCategoryItemPresenter;

/**
 * 描述：选择目录的具体事项界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseCategoryItemActivity extends BaseActivity implements ChooseCategoryItemContract.View{

    private ChooseCategoryItemContract.Presenter mPresenter;
    private LinearLayout mQualityCheckListView,mModelView,mBluePrintView,mQualityCheckModuleVIew,mCreateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_choose_category_item_activity);
        initView();
        setListener();
        initDataForActivity();
    }

    private void initView() {
        mQualityCheckListView = (LinearLayout) findViewById(R.id.choose_category_item_item_zjqd);
        mModelView = (LinearLayout) findViewById(R.id.choose_category_item_item_mx);
        mBluePrintView = (LinearLayout) findViewById(R.id.choose_category_item_item_tz);
        mQualityCheckModuleVIew = (LinearLayout) findViewById(R.id.choose_category_item_item_zjxm);
        mCreateView = (LinearLayout) findViewById(R.id.choose_category_item_item_create);

        //计算每个方形的宽度
        int width = (ScreenUtil.getScreenInfo()[0]-ScreenUtil.dp2px(80))/3;
        mQualityCheckListView.getLayoutParams().height = width;
        mQualityCheckListView.getLayoutParams().width = width;
        mModelView.getLayoutParams().height = width;
        mModelView.getLayoutParams().width = width;
        mBluePrintView.getLayoutParams().height = width;
        mBluePrintView.getLayoutParams().width = width;
        mQualityCheckModuleVIew.getLayoutParams().height = width;
        mQualityCheckModuleVIew.getLayoutParams().width = width;
        mCreateView.getLayoutParams().height = width;
        mCreateView.getLayoutParams().width = width;
    }

    private void setListener() {

    }

    private void initDataForActivity() {
        mPresenter = new ChooseCategoryItemPresenter(this);
        mPresenter.initData(getIntent());
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
