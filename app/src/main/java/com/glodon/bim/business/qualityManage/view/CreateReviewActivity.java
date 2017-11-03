package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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
import com.glodon.bim.business.qualityManage.contract.CreateReviewContract;
import com.glodon.bim.business.qualityManage.presenter.CreateReviewPresenter;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.PhotoAlbumDialog;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.TNBImageItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：新建复查单  整改单
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateReviewActivity extends BaseActivity implements View.OnClickListener,CreateReviewContract.View{

    //状态
    private View mStatusView;
    //导航
    private TextView mCancelView,mTitleView,mSubmitView;
    //描述
    private EditText mDesView;
    private TextView mDesNumView;
    private LinearLayout mPhotoParent;
    private ImageView mPhoto0, mPhoto1, mPhoto2, mPhoto3;

    private PhotoAlbumDialog mPhotoAlbumDialog;
    //整改期限
    private ImageView mRemainFlag;
    private View mRemainLine;
    private RelativeLayout mRemainParent;
    private TextView mRemainName;
    //检查单问题
    private RelativeLayout mDetail;
    private ImageView mDetailIcon;
    private LinearLayout mDetailContent;

    //保存删除
    private Button mSaveView,mDeleteView;
    private CreateReviewContract.Presenter mPresenter;

    private QualityCheckListDetailView mDetailView;

    private long deptId,id;//项目id 和检查单id

    private String mImagePath;//照相传递的数据
    private String mCreateType; //创建的类型
    private AlbumData data;//相册传递的数据

    private boolean mIsShowPhoto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quality_create_review_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.create_review_status);
        //导航
        mCancelView = (TextView) findViewById(R.id.create_review_nav_cancel);
        mTitleView = (TextView) findViewById(R.id.create_review_nav_title);
        mSubmitView = (TextView) findViewById(R.id.create_review_nav_submit);
        //描述
        mDesView = (EditText) findViewById(R.id.create_review_description);
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
        //检查单问题
        mDetail = (RelativeLayout) findViewById(R.id.create_review_detail);
        mDetailIcon = (ImageView) findViewById(R.id.create_review_detail_icon);
        mDetailContent = (LinearLayout) findViewById(R.id.create_review_detail_content);
        //保存删除
        mSaveView = (Button) findViewById(R.id.create_review__save);
        mDeleteView = (Button) findViewById(R.id.create_review_delete);

    }


    private void setListener() {
        mCancelView.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mSubmitView,this);
        ThrottleClickEvents.throttleClick(mPhoto0,this);
        ThrottleClickEvents.throttleClick(mPhoto1,this);
        ThrottleClickEvents.throttleClick(mPhoto2,this);
        ThrottleClickEvents.throttleClick(mPhoto3,this);
        ThrottleClickEvents.throttleClick(mRemainFlag,this);
        ThrottleClickEvents.throttleClick(mRemainParent,this);
        ThrottleClickEvents.throttleClick(mDetail,this);
        ThrottleClickEvents.throttleClick(mSaveView,this);
        ThrottleClickEvents.throttleClick(mDeleteView,this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.create_review_nav_cancel:
                mActivity.finish();
                break;
            case R.id.create_review_nav_submit:

                break;
            case R.id.create_review_photo_0:

                break;
            case R.id.create_review_photo_1:

                break;
            case R.id.create_review_photo_2:

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
            case R.id.create_review_remain_flag:

                break;
            case R.id.create_review_remain:

                break;
            case R.id.create_review_detail:

                break;
            case R.id.create_review__save:

                break;
            case R.id.create_review_delete:

                break;
        }
    }

    private void initData() {
        //获取图片
        mIsShowPhoto = getIntent().getBooleanExtra(CommonConfig.SHOW_PHOTO,true);
        if(mIsShowPhoto){
            mPhotoParent.setVisibility(View.VISIBLE);
            mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
            data = (AlbumData) getIntent().getSerializableExtra(CommonConfig.ALBUM_DATA);
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
        }else{
            mPhotoParent.setVisibility(View.GONE);
        }

        mPresenter = new CreateReviewPresenter(this);
        deptId = getIntent().getLongExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID,0);
        id = getIntent().getLongExtra(CommonConfig.QUALITY_CHECK_LIST_ID,0);
        mPresenter.initData(getIntent());
        mDetailView = new QualityCheckListDetailView(mActivity,mDetailContent);
        mDetailView.getInfo(deptId,id);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter!=null)
        {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
        {
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
