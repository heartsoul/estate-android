package com.glodon.bim.business.qualityManage.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnChooseListListener;
import com.glodon.bim.business.qualityManage.presenter.CreateCheckListPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.TNBImageItem;
import com.glodon.bim.customview.datepicker.TNBCustomDatePickerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateCheckListActivity extends BaseActivity implements View.OnClickListener, CreateCheckListContract.View {


    private static final int REQUEST_CAMERA = 4;
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
//    private EditText mPhotoDescription;
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
    private TextView mModuleName;
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
    private LinearLayout mInputParent;
    private TextView mInputTitle, mLeftNumber;
    private View mInputBottomView;
    //图片删除 拖动到此处删除
    private LinearLayout mPhotoDelete;

    //提示框
    private SaveDeleteDialog mHintDialog;
    private SaveDeleteDialog mDeleteDialog;
    private SaveDeleteDialog mBackDialog;
    //图片集合
    private List<CreateCheckListParamsFile> mPhotoList;

    //输入法变化
    private int softHeight = 0;//输入法高度
    private LinearLayout rootLayout;//跟布局
    private int initHeight = 0;

    //是否显示图片   有的入口是无需图片直接创建
    private boolean mIsShowPhoto = true;

    //待提交时，从列表传递过来的参数
    private CreateCheckListParams mParams ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_create_check_list_activity);

        initView();

        setListener();

        initData();

        getInputMethodHeight();

    }

    private void initView() {
        mParams = (CreateCheckListParams) getIntent().getSerializableExtra(CommonConfig.CREATE_CHECK_LIST_PROPS);

        mStatusView = (LinearLayout) findViewById(R.id.create_check_list_status);
        rootLayout = (LinearLayout) findViewById(R.id.create_check_list_root);
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
//        mPhotoDescription = (EditText) findViewById(R.id.create_check_list_photo_desription);
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
        mModuleName = (TextView) findViewById(R.id.create_check_list_module_name);
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
        mInputParent = (LinearLayout) findViewById(R.id.create_check_list_input);
        mInputTitle = (TextView) findViewById(R.id.create_check_list_input_title);
        mLeftNumber = (TextView) findViewById(R.id.create_check_list_input_num_left);
        mInputBottomView = findViewById(R.id.create_check_list_input_bottom);
        //图片删除 拖动到此处删除
        mPhotoDelete = (LinearLayout) findViewById(R.id.create_check_list_photo_delete);


    }

    //如果前面传递过来数据了   则直接设置上
    private void setPropsInfo() {
        if(mParams!=null){
            mCompanyName.setText(mParams.constructionCompanyName);
            mPersonName.setText(mParams.responsibleUserName);
            mSiteDescription.setText(mParams.description);
            if(mParams.needRectification){
                mRemainFlag.setBackgroundResource(R.drawable.icon_flag_open);
                mRemainFlagState = true;
                mRemainName.setText(DateUtil.getNormalDate(Long.parseLong(mParams.lastRectificationDate)));
                mRemainParent.setVisibility(View.VISIBLE);
            }else{
                mRemainParent.setVisibility(View.GONE);
                mRemainFlagState = false;
                mRemainFlag.setBackgroundResource(R.drawable.icon_flag_close);
            }
            mModuleName.setText(mParams.qualityCheckpointName);
            showDeleteButton();
            mPresenter.setEditState(mParams);
        }else{
            mParams = new CreateCheckListParams();
        }
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSubmit, this);
        ThrottleClickEvents.throttleClick(mNavCheckLeftTitle, this, 1);
        ThrottleClickEvents.throttleClick(mNavCheckRightTitle, this, 1);

        ThrottleClickEvents.throttleClick(mCompanyParent, this, 1);
        ThrottleClickEvents.throttleClick(mPersonParent, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto0, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto1, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto2, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto3, this, 1);
        ThrottleClickEvents.throttleClick(mRemainFlag, this, 1);
        ThrottleClickEvents.throttleClick(mRemainParent, this, 1);
        ThrottleClickEvents.throttleClick(mModuleParent, this, 1);
        ThrottleClickEvents.throttleClick(mModuleBenchmark, this, 1);
        ThrottleClickEvents.throttleClick(mBluePrintParent, this, 1);
        ThrottleClickEvents.throttleClick(mModelParent, this, 1);
        ThrottleClickEvents.throttleClick(mSaveBtn, this, 1);
        ThrottleClickEvents.throttleClick(mDeleteBtn, this, 1);

        //输入现场描述字数的监听
        mSiteDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>255){
//                    mSiteDescription.setText(charSequence.toString().substring(0,255));
//                    mSiteDescription.setSelection(mSiteDescription.getText().toString().trim().length());
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                mLeftNumber.setText((255-mSiteDescription.getText().toString().trim().length())+"");
                String text = mSiteDescription.getText().toString().trim();
                int num ;
                if(TextUtils.isEmpty(text)){
                    num = 255;
                }else{
                    num = 255-text.length();
                }
                mLeftNumber.setText(num+"");
            }
        });
    }

    private void initData() {
        mPresenter = new CreateCheckListPresenter(this);
        setPropsInfo();
        mPresenter.initData(getIntent());
        //状态栏
        initStatusBar(mStatusView);

        //判断图片
        mIsShowPhoto = getIntent().getBooleanExtra(CommonConfig.SHOW_PHOTO,true);

        if (!mIsShowPhoto) {
            mPhotoParent.setVisibility(View.GONE);
        } else {
            mPhotoParent.setVisibility(View.VISIBLE);
            mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
            AlbumData data = (AlbumData) getIntent().getSerializableExtra(CommonConfig.ALBUM_DATA);

            LinkedHashMap<String,TNBImageItem> map = new LinkedHashMap<>();
            if(!TextUtils.isEmpty(mImagePath)){
                TNBImageItem item = new TNBImageItem();
                item.imagePath = mImagePath;
                map.put(mImagePath,item);
            }
            if(data!=null && data.map!=null){
                map = data.map;
            }
            showImages(map);
            mPresenter.setSelectedImages(map);
        }

        //默认单据 检查单
        mParams.inspectionType = CommonConfig.TYPE_INSPECTION;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            requestPermission(PERMISSIONS_STORAGE, REQUEST_CAMERA);
        }


    }

    /**
     * 输入法高度
     */
    private void getInputMethodHeight() {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);

                int screenHeight = rootLayout.getRootView().getHeight();
                softHeight = screenHeight - (r.bottom - r.top);

                //更改颜色框位置
                if (softHeight > screenHeight/3) {
                    mInputBottomView.getLayoutParams().height = softHeight-initHeight;
                    mInputParent.setVisibility(View.VISIBLE);
                } else {
                    initHeight = softHeight;
                    mInputParent.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.create_check_list_nav_back://返回按钮
                back();
                break;
            case R.id.create_check_list_nav_check_left_title://检查单
                mNavLeftLine.setVisibility(View.VISIBLE);
                mNavCheckLeftTitle.setTextSize(17);
                mNavRightLine.setVisibility(View.INVISIBLE);
                mNavCheckRightTitle.setTextSize(15);
                mParams.inspectionType = CommonConfig.TYPE_INSPECTION;
                break;
            case R.id.create_check_list_nav_check_right_title://验收单
                mNavLeftLine.setVisibility(View.INVISIBLE);
                mNavCheckLeftTitle.setTextSize(15);
                mNavRightLine.setVisibility(View.VISIBLE);
                mNavCheckRightTitle.setTextSize(17);
                mParams.inspectionType = CommonConfig.TYPE_ACCEPTANCE;
                break;
            case R.id.create_check_list_nav_submit://提交
                //必填  施工单位   责任人  现场描述  质检项目
                submit();
                break;
            case R.id.create_check_list_company://选择施工单位
                mPresenter.showCompanyList();
                break;
            case R.id.create_check_list_person://选择责任人
                mPresenter.getPersonList();
                break;
            case R.id.create_check_list_photo_0:
                mPresenter.toPreview(0);
                break;
            case R.id.create_check_list_photo_1:
                mPresenter.toPreview(1);
                break;
            case R.id.create_check_list_photo_2:
                mPresenter.toPreview(2);
                break;
            case R.id.create_check_list_photo_3://添加图片
                if (mPhotoAlbumDialog == null) {
                    mPhotoAlbumDialog = new PhotoAlbumDialog(mActivity);
                    mPhotoAlbumDialog.builder(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.takePhoto();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.openAlbum();
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
                TNBCustomDatePickerUtils.showDayDialog(mActivity, new TNBCustomDatePickerUtils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Map<String, Integer> map) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(map.get("year"), map.get("month") - 1, map.get("date"));
                        Date date = calendar.getTime();
                        String time = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
                        mRemainName.setText(time);
                        mParams.lastRectificationDate = calendar.getTimeInMillis()+"";
                    }
                });
                break;
            case R.id.create_check_list_module://选择质检项目
                mPresenter.toModuleList();
                break;
            case R.id.create_check_list_module_benchmark://质检项目标准

                break;
            case R.id.create_check_list_blueprint://关联图纸

                break;
            case R.id.create_check_list_model://管理模型

                break;
            case R.id.create_check_list_save://保存
                save();
                break;
            case R.id.create_check_list_delete://删除
                mDeleteDialog = new SaveDeleteDialog(mActivity);
                mDeleteDialog.getDeleteDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //调用接口删除
                        mPresenter.deleteCheckList();
                    }
                });
                mDeleteDialog.show();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        back();
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
        mChooseCompanyListDialog = new ChooseListDialog(mActivity, mCompanySelectPosition, "选择施工单位");
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
        mChoosePersonListDialog = new ChooseListDialog(mActivity, mPersonSelectPosition, "选择责任人");
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

    @Override
    public void showModuleName(String name) {
        mModuleName.setText(name);
    }

    @Override
    public void showDeleteButton() {
        mDeleteBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showImages(LinkedHashMap<String, TNBImageItem> mSelectedMap) {
        int size = mSelectedMap.size();
        List<ImageView> list = new ArrayList<>();
        list.add(mPhoto0);
        list.add(mPhoto1);
        list.add(mPhoto2);
        int position = 0;
        for(Map.Entry<String,TNBImageItem> entry:mSelectedMap.entrySet()){
            TNBImageItem item = entry.getValue();
            String url = item.thumbnailPath;
            if(TextUtils.isEmpty(url)){
                url = item.imagePath;
            }
            ImageLoader.showImageCenterCrop(mActivity,url,list.get(position),R.drawable.icon_default_image);
            position++;
            CameraUtil.frushStyemDCIM(mActivity,item.imagePath);
        }
        if(size==0){
            mPhoto0.setVisibility(View.GONE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if(size==1){
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if(size==2){
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if(size==3){
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.VISIBLE);
            mPhoto3.setVisibility(View.GONE);
        }
    }



    //点击返回按钮
    private void back(){
        if(isShowBackDialog()){
            mBackDialog = new SaveDeleteDialog(mActivity);
            mBackDialog.getBackDialog(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.finish();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save();
                }
            });
            mBackDialog.show();
        }else{
            mActivity.finish();
        }
    }

    //点击保存
    private void  save(){
        if(checkMustInfo()){
            assembleData();
            mPresenter.save(mParams);
        }
    }
    //点击提交
    private void submit(){
        if(checkMustInfo()) {
            assembleData();
            mPresenter.submit(mParams);
        }
    }
    //收集页面数据
    private void assembleData(){
        mParams.description = mSiteDescription.getText().toString().trim();
        mParams.needRectification = mRemainFlagState;
        mParams.files = mPhotoList;
    }

    /**
     * 检测必填项
     * return  true  所有必填项都已填写   false 有必填项没有填写
     */
    private boolean checkMustInfo() {
        List<String> temp = new ArrayList<>();
        String companyName = mCompanyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            mCompanyStar.setVisibility(View.VISIBLE);
            temp.add("施工单位");
        } else {
            mCompanyStar.setVisibility(View.INVISIBLE);
        }
//        mPersonName.setText("掌声");
        String personName = mPersonName.getText().toString().trim();
        if (TextUtils.isEmpty(personName)) {
            mPersonStar.setVisibility(View.VISIBLE);
            temp.add("责任人");
        } else {
            mPersonStar.setVisibility(View.INVISIBLE);
        }
        String siteContent = mSiteDescription.getText().toString().trim();
        if (TextUtils.isEmpty(siteContent)) {
            mSiteStar.setVisibility(View.VISIBLE);
            temp.add("现场描述");
        } else {
            mSiteStar.setVisibility(View.INVISIBLE);
        }
        String moduleName = mModuleName.getText().toString().trim();
        if (TextUtils.isEmpty(moduleName)) {
            mModuleStar.setVisibility(View.VISIBLE);
            temp.add("质检项目");
        } else {
            mModuleStar.setVisibility(View.INVISIBLE);
        }
        int size = temp.size();
        if (size > 0) {
            String content = "";
            //有必填项没有填写
            if (size == 1) {
                content = "您还未选择" + temp.get(0) + "!";
            }
            if (size == 2) {
                content = "您还未选择" + temp.get(0) + "和" + temp.get(1) + "!";
            }
            if (size == 3) {
                content = "您还未选择" + temp.get(0) + "、" + temp.get(1) + "和" + temp.get(2) + "!";
            }
            if (size == 4) {
                content = "您还未选择" + temp.get(0) + "、" + temp.get(1) + "、" + temp.get(2) + "和" + temp.get(3) + "!";
            }
            mHintDialog = new SaveDeleteDialog(mActivity);
            mHintDialog.getHintDialog(content);
            mHintDialog.show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否显示退出的提示
     */
    private boolean isShowBackDialog(){
        String temp = mCompanyName.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            return true;
        }
        temp = mPersonName.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            return true;
        }
        temp = mSiteDescription.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            return true;
        }
//        temp = mPhotoDescription.getText().toString().trim();
//        if(!TextUtils.isEmpty(temp)){
//            return true;
//        }

        if(mPhotoList!=null && mPhotoList.size()>0){
            return true;
        }

        if(mRemainFlagState){
            temp = mRemainName.getText().toString().trim();
            if(!TextUtils.isEmpty(temp)){
                return true;
            }
        }
        temp = mModelName.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            return true;
        }
        temp = mBluePrintName.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            return true;
        }
        temp = mModelName.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            return true;
        }
        return false;
    }
}
