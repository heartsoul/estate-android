package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.contract.CreateReviewContract;
import com.glodon.bim.business.qualityManage.presenter.CreateReviewPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.TNBImageItem;
import com.glodon.bim.customview.datepicker.TNBCustomDatePickerUtils;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：新建复查单  整改单
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateReviewActivity extends BaseActivity implements View.OnClickListener, CreateReviewContract.View {

    //状态
    private View mStatusView;
    //导航
    private RelativeLayout mCancelView, mSubmitView;
    private TextView mTitleView;
    //描述
    private EditText mDesView;
    private TextView mDesNumView;
    private ImageView mDesStar;
    //图片
    private LinearLayout mPhotoParent;
    private ImageView mPhoto0, mPhoto1, mPhoto2, mPhoto3;

    private PhotoAlbumDialog mPhotoAlbumDialog;
    //整改期限
    private ImageView mRemainFlag;
    private View mRemainLine;
    private RelativeLayout mRemainParent;
    private TextView mRemainName;
    private LinearLayout mRemain;
    //检查单问题
    private RelativeLayout mDetail;
    private ImageView mDetailIcon;
    private LinearLayout mDetailContent;
    private View mDetailContentLine;

    //保存删除
    private Button mSaveView, mDeleteView;
    private CreateReviewContract.Presenter mPresenter;

    private QualityCheckListDetailView mDetailView;


    private String mImagePath;//照相传递的数据
    private AlbumData data;//相册传递的数据

    private boolean mIsShowPhoto = true;//是否显示图片
    private boolean mIsUpToStandard = false;//是否合格
    private String mCurrentStatus = CommonConfig.STATUS_NOT_ACCEPTED;//合格情况
    private String mSelectedTime = "";

    private SaveDeleteDialog mBackDialog;
    private SaveDeleteDialog mHintDialog;
    private SaveDeleteDialog mDeleteDialog;

    private String mCreateType;//当前的单据类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quality_create_review_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.create_review_status);
        //导航
        mCancelView = (RelativeLayout) findViewById(R.id.create_review_nav_cancel);
        mTitleView = (TextView) findViewById(R.id.create_review_nav_title);
        mSubmitView = (RelativeLayout) findViewById(R.id.create_review_nav_submit);
        //描述
        mDesView = (EditText) findViewById(R.id.create_review_description);
        mDesStar = (ImageView) findViewById(R.id.create_review_description_star);
        mDesNumView = (TextView) findViewById(R.id.create_review_description_num);
        mPhotoParent = (LinearLayout) findViewById(R.id.create_review_photo_parent);
        mPhoto0 = (ImageView) findViewById(R.id.create_review_photo_0);
        mPhoto1 = (ImageView) findViewById(R.id.create_review_photo_1);
        mPhoto2 = (ImageView) findViewById(R.id.create_review_photo_2);
        mPhoto3 = (ImageView) findViewById(R.id.create_review_photo_3);
        //整改期限
        mRemainFlag = (ImageView) findViewById(R.id.create_review_remain_flag);
        mRemainLine = findViewById(R.id.create_review_remain_line);
        mRemainParent = (RelativeLayout) findViewById(R.id.create_review_remain);
        mRemainName = (TextView) findViewById(R.id.create_review_remain_name);
        mRemain = (LinearLayout) findViewById(R.id.create_review_remain_parent);
        //检查单问题
        mDetail = (RelativeLayout) findViewById(R.id.create_review_detail);
        mDetailIcon = (ImageView) findViewById(R.id.create_review_detail_icon);
        mDetailContent = (LinearLayout) findViewById(R.id.create_review_detail_content);
        mDetailContentLine = findViewById(R.id.create_review_detail_content_line);
        //保存删除
        mSaveView = (Button) findViewById(R.id.create_review__save);
        mDeleteView = (Button) findViewById(R.id.create_review_delete);

    }


    private void setListener() {
        mCancelView.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mSubmitView, this);
        ThrottleClickEvents.throttleClick(mPhoto0, this);
        ThrottleClickEvents.throttleClick(mPhoto1, this);
        ThrottleClickEvents.throttleClick(mPhoto2, this);
        ThrottleClickEvents.throttleClick(mPhoto3, this);
        ThrottleClickEvents.throttleClick(mRemainFlag, this, 1);
        ThrottleClickEvents.throttleClick(mRemainParent, this, 1);
        ThrottleClickEvents.throttleClick(mDetail, this, 1);
        ThrottleClickEvents.throttleClick(mSaveView, this);
        ThrottleClickEvents.throttleClick(mDeleteView, this);

        mDesView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = mDesView.getText().toString();
                int num;
                if (TextUtils.isEmpty(text)) {
                    num = 255;
                } else {
                    num = 255 - text.length();
                }
                mDesNumView.setText(num + "字");
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.create_review_nav_cancel:
                back();
                break;
            case R.id.create_review_nav_submit:
                submit();
                break;
            case R.id.create_review_photo_0:
                mPresenter.toPreview(0);
                break;
            case R.id.create_review_photo_1:
                mPresenter.toPreview(1);
                break;
            case R.id.create_review_photo_2:
                mPresenter.toPreview(2);
                break;
            case R.id.create_review_photo_3:
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
            case R.id.create_review_remain_flag://是否合格
                if (mIsUpToStandard) {
                    mRemainParent.setVisibility(View.VISIBLE);
                    mRemainLine.setVisibility(View.VISIBLE);
                    mRemainFlag.setBackgroundResource(R.drawable.icon_flag_close);
                    mCurrentStatus = CommonConfig.STATUS_NOT_ACCEPTED;
                } else {
                    mRemainParent.setVisibility(View.GONE);
                    mRemainLine.setVisibility(View.GONE);
                    mRemainFlag.setBackgroundResource(R.drawable.icon_flag_open);
                    mCurrentStatus = CommonConfig.STATUS_CLOSED;
                }
                mIsUpToStandard = !mIsUpToStandard;
                break;
            case R.id.create_review_remain:
                TNBCustomDatePickerUtils.showDayDialog(mActivity, new TNBCustomDatePickerUtils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Map<String, Integer> map) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(map.get("year"), map.get("month") - 1, map.get("date"));
                        Date date = calendar.getTime();
                        String time = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
                        mRemainName.setText(time);
                        mSelectedTime = calendar.getTimeInMillis() + "";
                    }
                });
                break;
            case R.id.create_review_detail:
                if (mDetailContent.getVisibility() == View.VISIBLE) {
                    mDetailContent.setVisibility(View.GONE);
                    mDetailContentLine.setVisibility(View.GONE);
                    mDetailIcon.setBackgroundResource(R.drawable.icon_drawer_arrow_down);
                } else {
                    mDetailContent.setVisibility(View.VISIBLE);
                    mDetailContentLine.setVisibility(View.VISIBLE);
                    mDetailIcon.setBackgroundResource(R.drawable.icon_draw_arrow_up);
                }
                break;
            case R.id.create_review__save:
                save();
                break;
            case R.id.create_review_delete:
                delete();
                break;
        }
    }

    private void submit() {
        if (checkMustInfo()) {
            mPresenter.submit(mDesView.getText().toString(), mCurrentStatus, mSelectedTime);
        }
    }

    private void save() {
        if (checkMustInfo()) {
            mPresenter.save(mDesView.getText().toString(), mCurrentStatus, mSelectedTime);
        }
    }


    /**
     * 检测必填项
     * return  true  所有必填项都已填写   false 有必填项没有填写
     */
    private boolean checkMustInfo() {
        String desText = mDesView.getText().toString();
        String dateText = mRemainName.getText().toString().trim();
        if (CommonConfig.CREATE_TYPE_REVIEW.equals(mCreateType)) {
            if (mIsUpToStandard) {
                if (TextUtils.isEmpty(desText)) {
                    mHintDialog = new SaveDeleteDialog(mActivity);
                    mHintDialog.getHintDialog("您还未选择现场情况描述!");
                    mHintDialog.show();
                    return false;
                }
            } else {
                if (TextUtils.isEmpty(desText) && TextUtils.isEmpty(dateText)) {
                    mHintDialog = new SaveDeleteDialog(mActivity);
                    mHintDialog.getHintDialog("您还未选择现场情况描述和整改期限!");
                    mHintDialog.show();
                    return false;
                } else if (TextUtils.isEmpty(desText)) {
                    mHintDialog = new SaveDeleteDialog(mActivity);
                    mHintDialog.getHintDialog("您还未选择现场情况描述!");
                    mHintDialog.show();
                    return false;
                } else if (TextUtils.isEmpty(dateText)) {
                    mHintDialog = new SaveDeleteDialog(mActivity);
                    mHintDialog.getHintDialog("您还未选择整改期限!");
                    mHintDialog.show();
                    return false;
                }
            }

            if(!mIsUpToStandard) {
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
        }
        if (CommonConfig.CREATE_TYPE_REPAIR.equals(mCreateType)) {
            if (TextUtils.isEmpty(desText)) {
                mHintDialog = new SaveDeleteDialog(mActivity);
                mHintDialog.getHintDialog("您还未选择现场情况描述!");
                mHintDialog.show();
                return false;
            }
        }
        return true;
    }

    private void delete() {
        mDeleteDialog = new SaveDeleteDialog(mActivity);
        mDeleteDialog.getDeleteDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用接口删除
                mPresenter.delete();
            }
        });
        mDeleteDialog.show();
    }


    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (!isShowBackDialog()) {
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
        } else {
            mActivity.finish();
        }
    }

    private boolean isShowBackDialog() {
        String desText = mDesView.getText().toString();
        return mPresenter.isEqual(desText,mIsUpToStandard,mSelectedTime);
    }


    private void initData() {
        mPresenter = new CreateReviewPresenter(this);
        mPresenter.initData(getIntent());

        //获取图片
        showImages();

    }

    private void showImages() {
        mIsShowPhoto = getIntent().getBooleanExtra(CommonConfig.SHOW_PHOTO, true);
        if (mIsShowPhoto) {
            mPhotoParent.setVisibility(View.VISIBLE);
            mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
            data = (AlbumData) getIntent().getSerializableExtra(CommonConfig.ALBUM_DATA);
            LinkedHashList<String, TNBImageItem> map = new LinkedHashList<>();
            if (!TextUtils.isEmpty(mImagePath)) {
                TNBImageItem item = new TNBImageItem();
                item.imagePath = mImagePath;
                map.put(mImagePath, item);
            }
            if (data != null && data.map != null) {
                map = data.map;
            }
            showImages(map);
            mPresenter.setSelectedImages(map);
        } else {
            mPhotoParent.setVisibility(View.GONE);
        }
    }

    //检查单详情
    public void showDetail(long deptId, long id) {
        mDetailView = new QualityCheckListDetailView(mActivity, mDetailContent);
        mDetailView.getInfo(deptId, id);
    }


    //创建的类型  整改  /复查
    public void showTitleByType(String mCreateType) {
        this.mCreateType = mCreateType;
        if (CommonConfig.CREATE_TYPE_REPAIR.equals(mCreateType)) {
            mTitleView.setText("整改单");
            mRemain.setVisibility(View.GONE);
        } else if (CommonConfig.CREATE_TYPE_REVIEW.equals(mCreateType)) {
            mTitleView.setText("复查单");
            mRemain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDesAndImages(String description, List<QualityCheckListBeanItemFile> files) {
        mDesView.setText(description);
        if (files != null && files.size() > 0) {
            mPhotoParent.setVisibility(View.VISIBLE);
            LinkedHashList<String, TNBImageItem> mSelectedMap = new LinkedHashList<>();
            for (QualityCheckListBeanItemFile file : files) {
                TNBImageItem item = new TNBImageItem();
                item.imagePath = file.url;
                item.objectId = file.objectId;
                CreateCheckListParamsFile paramFile = new CreateCheckListParamsFile();
                paramFile.name = file.name;
                paramFile.extData = file.extData;
                paramFile.objectId = file.objectId;
//                paramFile.url = file.url;
                item.urlFile = paramFile;
                mSelectedMap.put(file.url, item);
            }
            showImages(mSelectedMap);
            mPresenter.setSelectedImages(mSelectedMap);
        } else {
            mPhotoParent.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRectificationInfo(String status, String lastRectificationDate) {
        mCurrentStatus = status;
        /**
         * 复查合格 "closed"
         *
         * 复查不合格  "notAccepted"
         */
        switch (status) {
            case CommonConfig.STATUS_CLOSED://合格
                mIsUpToStandard = true;
                mRemainParent.setVisibility(View.GONE);
                mRemainLine.setVisibility(View.GONE);
                mRemainFlag.setBackgroundResource(R.drawable.icon_flag_open);

                break;
            case CommonConfig.STATUS_NOT_ACCEPTED://不合格
                mIsUpToStandard = false;
                mRemainParent.setVisibility(View.VISIBLE);
                mRemainLine.setVisibility(View.VISIBLE);
                mRemainFlag.setBackgroundResource(R.drawable.icon_flag_close);
                mRemainName.setText(DateUtil.getNormalDate(Long.parseLong(lastRectificationDate)));
                mSelectedTime = lastRectificationDate;
                break;
        }
    }

    @Override
    public QualityCheckListDetailBean getDetailInfo() {
        return mDetailView.getDetailInfo();
    }

    @Override
    public void showDelete() {
        mDeleteView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showImages(LinkedHashList<String, TNBImageItem> mSelectedMap) {
        int size = mSelectedMap.size();
        List<ImageView> list = new ArrayList<>();
        list.add(mPhoto0);
        list.add(mPhoto1);
        list.add(mPhoto2);
        int position = 0;
        for (TNBImageItem entry : mSelectedMap.getValueList()) {
            TNBImageItem item = entry;
            String url = item.thumbnailPath;
            if (TextUtils.isEmpty(url)) {
                url = item.imagePath;
            }
            ImageLoader.showImageCenterCrop(mActivity, url, list.get(position), R.drawable.icon_default_image);
            position++;
            CameraUtil.frushStyemDCIM(mActivity, item.imagePath);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
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
