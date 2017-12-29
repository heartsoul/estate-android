package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnChooseListListener;
import com.glodon.bim.business.qualityManage.presenter.CreateCheckListPresenter;
import com.glodon.bim.business.qualityManage.util.IntentManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.ImageItem;
import com.glodon.bim.customview.datepicker.CustomDatePickerUtils;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateInspectionAcceptionFragment extends BaseFragment implements View.OnClickListener, CreateCheckListContract.View {


    private CreateCheckListContract.Presenter mPresenter;

    //检查单位
    private RelativeLayout mInspectionCompanyParent;
    private ImageView mInspectionCompanyStar;
    private TextView mInspectionCompanyName;
    private TextView mInspectionCompanyTitle;
    private String mInspectionCompanyTitleText;
    //施工单位
    private RelativeLayout mCompanyParent;
    private ImageView mCompanyStar;
    private TextView mCompanyName;

    //责任人
    private RelativeLayout mPersonParent;
    private ImageView mPersonStar;
    private TextView mPersonName;

    //现场描述
    private EditText mSiteDescription;
    private ImageView mSiteStar;
    //图片描述
    private LinearLayout mPhotoParent;
    private ImageView mPhoto0, mPhoto1, mPhoto2, mPhoto3;

    private PhotoAlbumDialog mPhotoAlbumDialog;
    //整改期限
    private ImageView mRemainFlag;
    private RelativeLayout mRemainParent;
    private TextView mRemainName;
    private ImageView mRemainStar;

    private boolean mRemainFlagState = true;
    //质检项目
    private RelativeLayout mModuleParent;
    private ImageView mModuleStar;
    private EditText mModuleName;
    private RelativeLayout mModuleBenchmark;
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
    private TextView mLeftNumber;
    private View mInputBottomView;
    //图片删除 拖动到此处删除
    private LinearLayout mPhotoDelete;

    //输入法变化
    private int initHeight = 0;


    //待提交时，从列表传递过来的参数
    private CreateCheckListParams mParams;
    private Intent intent;
    private String mInspetionType;

    //当施工单位和责任人为空时处理
    private String mCompanyEmptyText = "您需要去PC端添加施工单位数据";
    private String mPersonEmptyText = "您需要去PC端添加责任人数据";
    private boolean mIsCompanyAble = true;//是否可用
    private boolean mIsPersonAble = true;//是否可用
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflate(R.layout.quality_create_inspection_acception_view);
        initView(view);
        setListener();
        initData();
        return view;
    }

    //设置检查单位和验收单位名字  检查单-检查单位   验收单-验收单位
    public void setInspectionCompanyTitle(String title){
        mInspectionCompanyTitleText = title;
    }

    private void initView(View view) {

        //检查单位
        mInspectionCompanyParent =  view.findViewById(R.id.create_check_list_inspection_company);
        mInspectionCompanyName =  view.findViewById(R.id.create_check_list_inspection_company_name);
        mInspectionCompanyStar = view.findViewById(R.id.create_check_list_inspection_company_star);
        mInspectionCompanyTitle = view.findViewById(R.id.create_check_list_inspection_company_title);
        mInspectionCompanyTitle.setText(mInspectionCompanyTitleText);
        //施工单位
        mCompanyParent =  view.findViewById(R.id.create_check_list_company);
        mCompanyName =  view.findViewById(R.id.create_check_list_company_name);
        mCompanyStar =  view.findViewById(R.id.create_check_list_company_star);
        //责任人
        mPersonParent =  view.findViewById(R.id.create_check_list_person);
        mPersonName =  view.findViewById(R.id.create_check_list_person_name);
        mPersonStar = view.findViewById(R.id.create_check_list_person_star);
        //现场描述
        mSiteDescription =  view.findViewById(R.id.create_check_list_site_desription);
        mSiteStar =  view.findViewById(R.id.create_check_list_site_desription_star);
        //图片描述
        mPhotoParent =  view.findViewById(R.id.create_check_list_photo_parent);
        mPhoto0 =  view.findViewById(R.id.create_check_list_photo_0);
        mPhoto1 =  view.findViewById(R.id.create_check_list_photo_1);
        mPhoto2 =  view.findViewById(R.id.create_check_list_photo_2);
        mPhoto3 = view.findViewById(R.id.create_check_list_photo_3);
        //整改期限
        mRemainFlag =  view.findViewById(R.id.create_check_list_remain_flag);
        mRemainParent =  view.findViewById(R.id.create_check_list_remain);
        mRemainName = view.findViewById(R.id.create_check_list_remain_name);
        mRemainStar = view.findViewById(R.id.create_check_list_remain_star);
        //质检项目
        mModuleParent =  view.findViewById(R.id.create_check_list_module);
        mModuleStar =  view.findViewById(R.id.create_check_list_module_star);
        mModuleName =  view.findViewById(R.id.create_check_list_module_name);
        mModuleBenchmark = view.findViewById(R.id.create_check_list_module_benchmark);
        //关联图纸
        mBluePrintParent = view.findViewById(R.id.create_check_list_blueprint);
        mBluePrintName =  view.findViewById(R.id.create_check_list_blueprint_name);
        //关联模型
        mModelParent = view.findViewById(R.id.create_check_list_model);
        mModelName =  view.findViewById(R.id.create_check_list_model_name);
        //保存删除
        mSaveBtn =  view.findViewById(R.id.create_check_list_save);
        mDeleteBtn = view.findViewById(R.id.create_check_list_delete);
        //现场描述和图片描述  输入法弹出
        mInputParent = view.findViewById(R.id.create_check_list_input);
        mLeftNumber =  view.findViewById(R.id.create_check_list_input_num_left);
        mInputBottomView = view.findViewById(R.id.create_check_list_input_bottom);
        //图片删除 拖动到此处删除
        mPhotoDelete = view.findViewById(R.id.create_check_list_photo_delete);

        //默认为关闭  不需要整改
        clickRemain();
    }

    private void setListener() {

        ThrottleClickEvents.throttleClick(mInspectionCompanyParent, this, 1);
        ThrottleClickEvents.throttleClick(mCompanyParent, this, 1);
        ThrottleClickEvents.throttleClick(mPersonParent, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto0, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto1, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto2, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto3, this, 1);
        ThrottleClickEvents.throttleClick(mRemainFlag, this, 1);
        ThrottleClickEvents.throttleClick(mRemainParent, this, 1);
        ThrottleClickEvents.throttleClick(mModuleParent, this, 1);
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
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mSiteDescription.getText().toString();
                int num;
                if (TextUtils.isEmpty(text)) {
                    num = 255;
                } else {
                    num = 255 - text.length();
                }
                mLeftNumber.setText(num + "");
            }
        });

        mModuleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.moduleNameChanged(mModuleName.getText().toString().trim());
            }
        });
    }


    public void setIntentData(Intent intent){
        this.intent = intent;
    }

    public void setInspectionType(String type){
        this.mInspetionType = type;
    }

    private void initData() {
        mParams = (CreateCheckListParams) intent.getSerializableExtra(CommonConfig.CREATE_CHECK_LIST_PROPS);
        mPresenter = new CreateCheckListPresenter(this);
        setPropsInfo();
        mPresenter.initData(intent);
        //设置初始单据类型为检查单
        mPresenter.setInspectionType(CommonConfig.TYPE_INSPECTION);

        //是否显示图片   有的入口是无需图片直接创建
        boolean mIsShowPhoto = intent.getBooleanExtra(CommonConfig.SHOW_PHOTO, true);

        if (!mIsShowPhoto) {
            mPhotoParent.setVisibility(View.GONE);
        } else {
            mPhotoParent.setVisibility(View.VISIBLE);
            String mImagePath = intent.getStringExtra(CommonConfig.IAMGE_SAVE_PATH);//前面传递过来的图片路径
            AlbumData data = (AlbumData) intent.getSerializableExtra(CommonConfig.ALBUM_DATA);

            LinkedHashList<String, ImageItem> map = new LinkedHashList<>();
            if (!TextUtils.isEmpty(mImagePath)) {
                ImageItem item = new ImageItem();
                item.imagePath = mImagePath;
                map.put(mImagePath, item);
            }
            if (data != null && data.map != null) {
                map = data.map;
            }
            showImages(map);
            mPresenter.setSelectedImages(map);
        }

        //默认单据 检查单
        if(TextUtils.isEmpty(mParams.inspectionType)) {
            mParams.inspectionType = CommonConfig.TYPE_INSPECTION;
        }
        mPresenter.setInspectionType(mParams.inspectionType);
    }

    //如果前面传递过来数据了   则直接设置上
    private void setPropsInfo() {
        if (mParams != null && mInspetionType.equals(mParams.inspectionType)) {
            mInspectionCompanyName.setText(mParams.inspectionCompanyName);
            mCompanyName.setText(mParams.constructionCompanyName);
            mPersonName.setText(mParams.responsibleUserName);
            mSiteDescription.setText(mParams.description);
            if (mParams.needRectification) {
                mRemainFlag.setBackgroundResource(R.drawable.icon_flag_open);
                mRemainFlagState = true;
                mRemainName.setText(DateUtil.getNormalDate(Long.parseLong(mParams.lastRectificationDate)));
                mRemainParent.setVisibility(View.VISIBLE);
            } else {
                mRemainParent.setVisibility(View.GONE);
                mRemainFlagState = false;
                mRemainFlag.setBackgroundResource(R.drawable.icon_flag_close);
            }
            mModuleName.setText(mParams.qualityCheckpointName);
            if(mParams.qualityCheckpointId!=null && mParams.qualityCheckpointId.longValue()>0){
                showBenchMark();
                mModuleBenchmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentManager.toModuleStandard(getActivity(),mParams.qualityCheckpointId.longValue(),mParams.qualityCheckpointName);
                    }
                });
            }else{
                hideBenchMark();
            }
            showDeleteButton();
            mPresenter.setEditState(mParams);
        } else {
            mParams = new CreateCheckListParams();
            mParams.inspectionType = mInspetionType;
        }
    }

    private void showBenchMark(){
        mModuleBenchmark.setVisibility(View.VISIBLE);
    }
    private void hideBenchMark(){
        mModuleBenchmark.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.create_check_list_inspection_company://选择检查单位
                mPresenter.showInspectionCompanyList();
                break;
            case R.id.create_check_list_company://选择施工单位
                if(mIsCompanyAble) {
                    mPresenter.showCompanyList();
                }
                break;
            case R.id.create_check_list_person://选择责任人
                if(mIsPersonAble) {
                    mPresenter.getPersonList();
                }
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
                    mPhotoAlbumDialog = new PhotoAlbumDialog(getActivity());
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
                clickRemain();
                break;
            case R.id.create_check_list_remain://选择整改期限
                CustomDatePickerUtils.showDayDialog(getActivity(), new CustomDatePickerUtils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Map<String, Integer> map) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(map.get("year"), map.get("month") - 1, map.get("date"));
                        Date date = calendar.getTime();
                        String time = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
                        mRemainName.setText(time);
                        mParams.lastRectificationDate = calendar.getTimeInMillis() + "";
                    }
                });
                break;
            case R.id.create_check_list_module://选择质检项目
                mPresenter.toModuleList();
                break;
            case R.id.create_check_list_blueprint://关联图纸
                if(AppConfig.isShow) {
                    mPresenter.toBluePrint();
                }
                break;
            case R.id.create_check_list_model://关联模型
                if(AppConfig.isShow) {
                    mPresenter.toModelList();
                }
                break;
            case R.id.create_check_list_save://保存
                save();
                break;
            case R.id.create_check_list_delete://删除
                SaveDeleteDialog mDeleteDialog = new SaveDeleteDialog(getActivity());
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

    private void clickRemain() {
        if (mRemainFlagState) {
            mRemainFlag.setBackgroundResource(R.drawable.icon_flag_close);
            mRemainParent.setVisibility(View.GONE);
        } else {
            mRemainFlag.setBackgroundResource(R.drawable.icon_flag_open);
            mRemainParent.setVisibility(View.VISIBLE);
        }
        mRemainFlagState = !mRemainFlagState;
    }

    private void clickRightTitle() {
        mParams.inspectionType = CommonConfig.TYPE_ACCEPTANCE;
        mPresenter.setInspectionType(CommonConfig.TYPE_ACCEPTANCE);
    }

    private void clickLeftTitle() {
        mParams.inspectionType = CommonConfig.TYPE_INSPECTION;
        mPresenter.setInspectionType(CommonConfig.TYPE_INSPECTION);
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
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void showInspectionCompany(InspectionCompanyItem companyItem) {
        mInspectionCompanyName.setText(companyItem.name);
    }


    @Override
    public void showCompanyList(final List<String> mCompanyNameList, final int mCompanySelectPosition) {
        ChooseListDialog mChooseCompanyListDialog = new ChooseListDialog(getActivity(), mCompanySelectPosition, "选择施工单位");
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
    public void showInspectionCompanyList(final List<String> mInspectionCompanyNameList, final int mInspectionCompanySelectPosition) {
        ChooseListDialog mInspectionCompanyListDialog = new ChooseListDialog(getActivity(), mInspectionCompanySelectPosition, "选择检查单位");
        mInspectionCompanyListDialog.builder(new OnChooseListListener() {
            @Override
            public void onSelect(int position) {
                mInspectionCompanyName.setText(mInspectionCompanyNameList.get(position));
                mPresenter.setInspectionCompanySelectedPosition(position);

            }
        }, mInspectionCompanyNameList);
        mInspectionCompanyListDialog.show();
    }

    @Override
    public void showPersonList(final List<String> mPersonNameList, int mPersonSelectPosition) {
        ChooseListDialog mChoosePersonListDialog = new ChooseListDialog(getActivity(), mPersonSelectPosition, "选择责任人");
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
    public void showModuleName(final String name, final long id) {
        mModuleName.setText(name);
        showBenchMark();
        mModuleBenchmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentManager.toModuleStandard(getActivity(),id,name);
            }
        });
    }

    @Override
    public String getBluePrintName() {
        return mBluePrintName.getText().toString().trim();
    }

    @Override
    public String getModelElementName() {
        return mModelName.getText().toString().trim();
    }

    @Override
    public void showBluePrintName(String name, String id) {
        mBluePrintName.setText(Html.fromHtml(name));
    }

    @Override
    public void showModelName(String name) {
        mModelName.setText(Html.fromHtml(name));
    }

    @Override
    public void showDeleteButton() {
        mDeleteBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showImages(LinkedHashList<String, ImageItem> mSelectedMap) {
        int size = mSelectedMap.size();
        List<ImageView> list = new ArrayList<>();
        list.add(mPhoto0);
        list.add(mPhoto1);
        list.add(mPhoto2);
        int position = 0;
        for (ImageItem entry : mSelectedMap.getValueList()) {
            ImageItem item = entry;
            String url = item.thumbnailPath;
            if (TextUtils.isEmpty(url)) {
                url = item.imagePath;
            }
            ImageLoader.showImageCenterCrop(getActivity(), url, list.get(position), R.drawable.icon_default_image);
            position++;
            CameraUtil.frushStyemDCIM(getActivity(), item.imagePath);
        }
        if (size == 0) {
            mPhoto0.setVisibility(View.GONE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if (size == 1) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if (size == 2) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if (size == 3) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.VISIBLE);
            mPhoto3.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitle(String mInspectionType) {
        switch (mInspectionType) {
            case CommonConfig.TYPE_INSPECTION:
                clickLeftTitle();
                break;
            case CommonConfig.TYPE_ACCEPTANCE:
                clickRightTitle();
                break;
        }
    }

    @Override
    public String getModuleName() {
        return mModuleName.getText().toString().trim();
    }

    @Override
    public void showModuleBenchMark(boolean isshow, final long templateId) {
        if(isshow)
        {
            showBenchMark();
            mModuleBenchmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentManager.toModuleStandard(getActivity(),templateId,getModuleName());
                }
            });

        }else{
            hideBenchMark();
        }
    }


    @Override
    public void showEmptyCompany() {
        mCompanyName.setText(mCompanyEmptyText);
        mPersonName.setText(mPersonEmptyText);
        mCompanyName.setTextColor(getResources().getColor(R.color.c_acacac));
        mPersonName.setTextColor(getResources().getColor(R.color.c_acacac));
        mIsCompanyAble = false;
        mIsPersonAble = false;
    }

    @Override
    public void showPersonEmpty() {
        mPersonName.setText(mPersonEmptyText);
        mPersonName.setTextColor(getResources().getColor(R.color.c_acacac));
    }




    //点击返回按钮
    public void back() {
        if (isShowBackDialog()) {
            SaveDeleteDialog mBackDialog = new SaveDeleteDialog(getActivity());
            mBackDialog.getBackDialog(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPresenter.isChange()) {
                        getActivity().setResult(Activity.RESULT_OK);
                    }
                    getActivity().finish();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save();
                }
            });
            mBackDialog.show();
        } else {
            if (mPresenter.isChange()) {
                getActivity().setResult(Activity.RESULT_OK);
            }
            getActivity().finish();
        }
    }

    //点击保存
    private void save() {
        if (checkMustInfo()) {
            assembleData();
            mPresenter.save(mParams);
        }
    }

    //点击提交
    public void submit() {
        if (checkMustInfo()) {
            assembleData();
            mPresenter.submit(mParams);
        }
    }

    //收集页面数据
    private void assembleData() {
        mParams.description = mSiteDescription.getText().toString();
        mParams.needRectification = mRemainFlagState;
        mPresenter.setCurrentModuleName(mModuleName.getText().toString().trim());
    }

    /**
     * 检测必填项
     * return  true  所有必填项都已填写   false 有必填项没有填写
     */
    private boolean checkMustInfo() {
        List<String> temp = new ArrayList<>();
        String inspectionCompanyName = mInspectionCompanyName.getText().toString().trim();
        if (TextUtils.isEmpty(inspectionCompanyName)) {
            mInspectionCompanyStar.setVisibility(View.VISIBLE);
            temp.add("检查单位");
        } else {
            mInspectionCompanyStar.setVisibility(View.INVISIBLE);
        }
        String companyName = mCompanyName.getText().toString().trim();
        if(mCompanyEmptyText.equals(companyName)){
            companyName = "";
        }
        if (TextUtils.isEmpty(companyName)) {
            mCompanyStar.setVisibility(View.VISIBLE);
            temp.add("施工单位");
        } else {
            mCompanyStar.setVisibility(View.INVISIBLE);
        }
        String personName = mPersonName.getText().toString().trim();
        if(mPersonEmptyText.equals(personName)){
            personName = "";
        }
        if (TextUtils.isEmpty(personName)) {
            mPersonStar.setVisibility(View.VISIBLE);
            temp.add("责任人");
        } else {
            mPersonStar.setVisibility(View.INVISIBLE);
        }
        String siteContent = mSiteDescription.getText().toString();
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

        if (mRemainFlagState) {
            String dateText = mRemainName.getText().toString().trim();
            if (TextUtils.isEmpty(dateText)) {
                mRemainStar.setVisibility(View.VISIBLE);
                temp.add("整改期限");
            } else {
                mRemainStar.setVisibility(View.INVISIBLE);
            }
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
            if (size == 5) {
                content = "您还未选择" + temp.get(0) + "、" + temp.get(1) + "、" + temp.get(2) + "、" + temp.get(3) + "和" + temp.get(4) + "!";
            }
            if (size == 6) {
                content = "您还未选择" + temp.get(0) + "、" + temp.get(1) + "、" + temp.get(2) + "、" + temp.get(3) + "、" + temp.get(4) + "和" + temp.get(5) + "!";
            }
            SaveDeleteDialog mHintDialog = new SaveDeleteDialog(getActivity());
            mHintDialog.getHintDialog(content);
            mHintDialog.show();
            return false;
        }

        if (mRemainFlagState) {
            String dateText = mRemainName.getText().toString().trim();
            if (!TextUtils.isEmpty(dateText)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    long millionSeconds = sdf.parse(dateText).getTime();//毫秒
                    Calendar calendar = Calendar.getInstance();
                    int YY = calendar.get(Calendar.YEAR);
                    int MM = calendar.get(Calendar.MONTH) + 1;
                    int DD = calendar.get(Calendar.DATE);
                    long today = sdf.parse(YY + "-" + MM + "-" + DD).getTime();
                    if (millionSeconds < today) {
                        ToastManager.show("整改期限不能早于当前日期！");
                        return false;
                    }
                } catch (ParseException e) {
                    LogUtil.e(e.getMessage());
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 是否显示退出的提示
     */
    private boolean isShowBackDialog() {
        assembleData();
        return mPresenter.isDifferent(mParams);
    }



    public void onGlobalLayout(LinearLayout rootLayout) {
        Rect r = new Rect();
        rootLayout.getWindowVisibleDisplayFrame(r);

        int screenHeight = rootLayout.getRootView().getHeight();
        int softHeight = screenHeight - (r.bottom - r.top);//输入法高度

        //更改颜色框位置
        if (softHeight > screenHeight / 3) {
            mInputBottomView.getLayoutParams().height = softHeight - initHeight;
            if(mInputParent!=null) {
                mInputParent.setVisibility(View.VISIBLE);
            }
        } else {
            initHeight = softHeight;
            if(mInputParent!=null) {
                mInputParent.setVisibility(View.GONE);
            }
        }
    }
}
