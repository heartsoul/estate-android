package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnChooseListListener;
import com.glodon.bim.business.qualityManage.presenter.CreateCheckListPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.PhotoAlbumDialog;
import com.glodon.bim.customview.datepicker.TNBCustomDatePickerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CreateCheckListActivity extends BaseActivity implements View.OnClickListener, CreateCheckListContract.View {

    private CreateCheckListContract.Presenter mPresenter;

    private String mImagePath;//前面传递过来的图片路径
    private LinearLayout mStatusView;//状态栏
    //导航栏
    private ImageView mNavBack;
    private TextView mNavSubmit, mNavCheckLeftTitle, mNavCheckRightTitle;
    private View mNavLeftLine, mNavRightLine;
    //内容

    //施工单位
    private RelativeLayout mCompanyParent;
    private ImageView mCompanyStar;
    private TextView mCompanyName;

    private ChooseListDialog mChooseCompanyListDialog;
    //责任人
    private RelativeLayout mPersonParent;
    private ImageView mPersonStar;
    private TextView mPersonName;

    private ChooseListDialog mChoosePersonListDialog;
    //现场描述
    private EditText mSiteDescription;
    private ImageView mSiteStar;
    //图片描述
    private EditText mPhotoDescription;
    private LinearLayout mPhotoParent;
    private ImageView mPhoto0, mPhoto1, mPhoto2, mPhoto3;

    private PhotoAlbumDialog mPhotoAlbumDialog;
    //整改期限
    private ImageView mRemainFlag;
    private RelativeLayout mRemainParent;
    private TextView mRemainName;

    private boolean mRemainFlagState = true;
    //质检项目
    private RelativeLayout mModuleParent;
    private ImageView mModuleStar;
    private EditText mModuleName;
    private ImageView mModuleBenchmark;
    //关联图纸
    private RelativeLayout mBluePrintParent;
    private TextView mBluePrintName;
    //关联模型
    private RelativeLayout mModelParent;
    private TextView mModelName;
    //保存删除
    private Button mSaveBtn, mDeleteBtn;
    //现场描述和图片描述  输入法弹出
    private RelativeLayout mInputParent;
    private TextView mInputTitle, mLeftNumber;
    //图片删除 拖动到此处删除
    private LinearLayout mPhotoDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_create_check_list_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        mStatusView = (LinearLayout) findViewById(R.id.create_check_list_status);
        //导航栏
        mNavBack = (ImageView) findViewById(R.id.create_check_list_nav_back);
        mNavSubmit = (TextView) findViewById(R.id.create_check_list_nav_submit);
        mNavCheckLeftTitle = (TextView) findViewById(R.id.create_check_list_nav_check_left_title);
        mNavCheckRightTitle = (TextView) findViewById(R.id.create_check_list_nav_check_right_title);
        mNavLeftLine = findViewById(R.id.create_check_list_nav_check_left_line);
        mNavRightLine = findViewById(R.id.create_check_list_nav_check_right_line);

        //施工单位
        mCompanyParent = (RelativeLayout) findViewById(R.id.create_check_list_company);
        mCompanyName = (TextView) findViewById(R.id.create_check_list_company_name);
        mCompanyStar = (ImageView) findViewById(R.id.create_check_list_company_star);
        //责任人
        mPersonParent = (RelativeLayout) findViewById(R.id.create_check_list_person);
        mPersonName = (TextView) findViewById(R.id.create_check_list_person_name);
        mPersonStar = (ImageView) findViewById(R.id.create_check_list_person_star);
        //现场描述
        mSiteDescription = (EditText) findViewById(R.id.create_check_list_site_desription);
        mSiteStar = (ImageView) findViewById(R.id.create_check_list_site_desription_star);
        //图片描述
        mPhotoDescription = (EditText) findViewById(R.id.create_check_list_photo_desription);
        mPhotoParent = (LinearLayout) findViewById(R.id.create_check_list_photo_parent);
        mPhoto0 = (ImageView) findViewById(R.id.create_check_list_photo_0);
        mPhoto1 = (ImageView) findViewById(R.id.create_check_list_photo_1);
        mPhoto2 = (ImageView) findViewById(R.id.create_check_list_photo_2);
        mPhoto3 = (ImageView) findViewById(R.id.create_check_list_photo_3);
        //整改期限
        mRemainFlag = (ImageView) findViewById(R.id.create_check_list_remain_flag);
        mRemainParent = (RelativeLayout) findViewById(R.id.create_check_list_remain);
        mRemainName = (TextView) findViewById(R.id.create_check_list_remain_name);
        //质检项目
        mModuleParent = (RelativeLayout) findViewById(R.id.create_check_list_module);
        mModuleStar = (ImageView) findViewById(R.id.create_check_list_module_star);
        mModuleName = (EditText) findViewById(R.id.create_check_list_module_name);
        mModuleBenchmark = (ImageView) findViewById(R.id.create_check_list_module_benchmark);
        //关联图纸
        mBluePrintParent = (RelativeLayout) findViewById(R.id.create_check_list_blueprint);
        mBluePrintName = (TextView) findViewById(R.id.create_check_list_blueprint_name);
        //关联模型
        mModelParent = (RelativeLayout) findViewById(R.id.create_check_list_model);
        mModelName = (TextView) findViewById(R.id.create_check_list_model_name);
        //保存删除
        mSaveBtn = (Button) findViewById(R.id.create_check_list_save);
        mDeleteBtn = (Button) findViewById(R.id.create_check_list_delete);
        //现场描述和图片描述  输入法弹出
        mInputParent = (RelativeLayout) findViewById(R.id.create_check_list_input);
        mInputTitle = (TextView) findViewById(R.id.create_check_list_input_title);
        mLeftNumber = (TextView) findViewById(R.id.create_check_list_input_num_left);
        //图片删除 拖动到此处删除
        mPhotoDelete = (LinearLayout) findViewById(R.id.create_check_list_photo_delete);
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSubmit, this);
        ThrottleClickEvents.throttleClick(mNavCheckLeftTitle, this, 1);
        ThrottleClickEvents.throttleClick(mNavCheckRightTitle, this, 1);

        ThrottleClickEvents.throttleClick(mCompanyParent, this, 1);
        ThrottleClickEvents.throttleClick(mPersonParent, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto3, this, 1);
        ThrottleClickEvents.throttleClick(mRemainFlag, this, 1);
        ThrottleClickEvents.throttleClick(mRemainParent, this, 1);
        ThrottleClickEvents.throttleClick(mModuleParent, this, 1);
        ThrottleClickEvents.throttleClick(mModuleBenchmark, this, 1);
        ThrottleClickEvents.throttleClick(mBluePrintParent, this, 1);
        ThrottleClickEvents.throttleClick(mModelParent, this, 1);
        ThrottleClickEvents.throttleClick(mSaveBtn, this, 1);
        ThrottleClickEvents.throttleClick(mDeleteBtn, this, 1);
    }

    private void initData() {
        mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
        initStatusBar(mStatusView);

        mPresenter = new CreateCheckListPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.create_check_list_nav_back://返回按钮
                mActivity.finish();
                break;
            case R.id.create_check_list_nav_check_left_title://检查单
                mNavLeftLine.setVisibility(View.VISIBLE);
                mNavCheckLeftTitle.setTextSize(17);
                mNavRightLine.setVisibility(View.INVISIBLE);
                mNavCheckRightTitle.setTextSize(15);
                break;
            case R.id.create_check_list_nav_check_right_title://验收单
                mNavLeftLine.setVisibility(View.INVISIBLE);
                mNavCheckLeftTitle.setTextSize(15);
                mNavRightLine.setVisibility(View.VISIBLE);
                mNavCheckRightTitle.setTextSize(17);
                break;
            case R.id.create_check_list_nav_submit://提交
                //必填  施工单位   责任人  现场描述  质检项目
                break;
            case R.id.create_check_list_company://选择施工单位
                mPresenter.showCompanyList();
                break;
            case R.id.create_check_list_person://选择责任人
                mPresenter.getPersonList();
                break;
            case R.id.create_check_list_photo_3://添加图片
                if (mPhotoAlbumDialog == null) {
                    mPhotoAlbumDialog = new PhotoAlbumDialog(mActivity);
                    mPhotoAlbumDialog.builder(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                mPhotoAlbumDialog.show();
                break;
            case R.id.create_check_list_remain_flag://整改期限开关
                if (mRemainFlagState) {
                    mRemainFlag.setBackgroundResource(R.drawable.icon_flag_close);
                    mRemainParent.setVisibility(View.GONE);
                } else {
                    mRemainFlag.setBackgroundResource(R.drawable.icon_flag_open);
                    mRemainParent.setVisibility(View.VISIBLE);
                }
                mRemainFlagState = !mRemainFlagState;
                break;
            case R.id.create_check_list_remain://选择整改期限
                TNBCustomDatePickerUtils.showDayPicker(mActivity, new TNBCustomDatePickerUtils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Map<String, Integer> map) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(map.get("year"), map.get("month") - 1, map.get("date"));
                        Date date = calendar.getTime();
                        String time = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
                        LogUtil.e("当前选择的时间为：-------------" + time);
                        mRemainName.setText(time);
                    }
                });
                break;
            case R.id.create_check_list_module://选择质检项目

                break;
            case R.id.create_check_list_module_benchmark://质检项目标准

                break;
            case R.id.create_check_list_blueprint://关联图纸

                break;
            case R.id.create_check_list_model://管理模型

                break;
            case R.id.create_check_list_save://保存

                break;
            case R.id.create_check_list_delete://删除

                break;
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
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showCompany(CompanyItem companyItem) {
        mCompanyName.setText(companyItem.name);
    }

    @Override
    public void showCompanyList(final List<String> mCompanyNameList, final int mCompanySelectPosition) {
        mChooseCompanyListDialog = new ChooseListDialog(mActivity, mCompanySelectPosition,"选择施工单位");
        mChooseCompanyListDialog.builder(new OnChooseListListener() {
            @Override
            public void onSelect(int position) {
                mCompanyName.setText(mCompanyNameList.get(position));
                mPresenter.setCompanySelectedPosition(position);
                //更改施工单位  更改责任人
                if (mCompanySelectPosition != position) {
                    mPersonName.setText("");
                    mPresenter.setPersonSelectedPosition(-1);
                }
            }
        }, mCompanyNameList);
        mChooseCompanyListDialog.show();
    }

    @Override
    public void showPersonList(final List<String> mPersonNameList, int mPersonSelectPosition) {
        mChoosePersonListDialog = new ChooseListDialog(mActivity, mPersonSelectPosition,"选择责任人");
        mChoosePersonListDialog.builder(new OnChooseListListener() {
            @Override
            public void onSelect(int position) {
                if (position >= 0 && position < mPersonNameList.size()) {
                    mPersonName.setText(mPersonNameList.get(position));
                }
                mPresenter.setPersonSelectedPosition(position);
            }
        }, mPersonNameList);
        mChoosePersonListDialog.show();
    }
}
