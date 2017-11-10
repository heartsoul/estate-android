package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailInspectionInfo;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailProgressInfo;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailViewContract;
import com.glodon.bim.business.qualityManage.listener.OnShowQualityCheckDetailListener;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailViewPresenter;
import com.glodon.bim.customview.dialog.LoadingDialogManager;

import java.util.List;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityCheckListDetailView implements QualityCheckListDetailViewContract.View{

    //检查单问题
    private LinearLayout mParent;
    private TextView mCompanyView,mPersonView,mModuleView,mSiteDescription,mSaveTimeView,mSubmitTimeView,mBluePrintView,mModelView;
    private ImageView mModuleBenchMarkView;

    private QualityCheckListDetailBean mBean;

    private Activity mActivity;

    private View view;

    private QualityCheckListDetailViewContract.Presenter mPresenter;

    private LoadingDialogManager mLoadingDialog;

    private OnShowQualityCheckDetailListener mListener;

    public void setmListener(OnShowQualityCheckDetailListener mListener) {
        this.mListener = mListener;
    }

    public QualityCheckListDetailView(Activity mActivity, View view) {
        this.mActivity = mActivity;
        this.view = view;
        initView();
    }


    private  void initView() {
        mParent = view.findViewById(R.id.quality_check_list_detail_list);
        mModuleBenchMarkView =view.findViewById(R.id.quality_check_list_detail_benchmark);
        mCompanyView = view.findViewById(R.id.quality_check_list_detail_company);
        mPersonView = view.findViewById(R.id.quality_check_list_detail_person);
        mModuleView = view.findViewById(R.id.quality_check_list_detail_module);
        mSiteDescription = view.findViewById(R.id.quality_check_list_detail_site_description);
        mSaveTimeView = view.findViewById(R.id.quality_check_list_detail_time_save);
        mSubmitTimeView =  view.findViewById(R.id.quality_check_list_detail_time_submit);
        mBluePrintView = view.findViewById(R.id.quality_check_list_detail_blueprint);
        mModelView = view.findViewById(R.id.quality_check_list_detail_model);
    }

    public void getInfo(long deptId,long id){
        mPresenter = new QualityCheckListDetailViewPresenter(this);
        mPresenter.getInspectInfo(deptId,id);
    }

    public void updateInfo(long deptId,long id){
        mPresenter.getInspectInfo(deptId,id);
    }
    @Override
    public void updateData(QualityCheckListDetailBean bean) {
        mBean = bean;
        mParent.removeAllViews();
        addBasicInfo();
        addCheckDescription();
        List<QualityCheckListDetailProgressInfo> list = mBean.progressInfos;
        if(list!=null && list.size()>0){
            for(int i=list.size()-1;i>=0;i--){
                QualityCheckListDetailProgressInfo info = list.get(i);
                addHistoryDescription(info);
            }
        }
        if(mListener!=null){
            mListener.onDetailShow(bean);
        }
    }

    //添加基本信息
    private void addBasicInfo() {
        QualityCheckListDetailInspectionInfo info = mBean.inspectionInfo;
        mCompanyView.setText(info.constructionCompanyName);
        mPersonView.setText(info.responsibleUserName);
        mModuleView.setText(info.qualityCheckpointName);
        mSiteDescription.setText(info.description);
        mSaveTimeView.setText(DateUtil.getNormalTime(Long.parseLong(info.createTime)));
        mSubmitTimeView.setText(DateUtil.getNormalTime(Long.parseLong(info.updateTime)));
        mBluePrintView.setText("");
        mModelView.setText(info.elementName);
    }

    //添加检查时的问题
    private  void addCheckDescription(){
        View view = LayoutInflater.from(mActivity).inflate(R.layout.quality_check_list_detail_item_view,null);
        mParent.addView(view);
        ImageView headIcon = view.findViewById(R.id.quality_check_list_detail_item_head);
        TextView nameView = view.findViewById(R.id.quality_check_list_detail_item_name);
        TextView timeView = view.findViewById(R.id.quality_check_list_detail_item_time);
        TextView statusView = view.findViewById(R.id.quality_check_list_detail_item_status);
        TextView desView = view.findViewById(R.id.quality_check_list_detail_item_text);
        TextView timeLimitView = view.findViewById(R.id.quality_check_list_detail_item_time_limit);
        ImageView oneImageView = view.findViewById(R.id.quality_check_list_detail_item_image_one);
        LinearLayout imageParent = view.findViewById(R.id.quality_check_list_detail_item_image_parent);
        ImageView image0 = view.findViewById(R.id.quality_check_list_detail_item_image_0);
        ImageView image1 = view.findViewById(R.id.quality_check_list_detail_item_image_1);
        ImageView image2 = view.findViewById(R.id.quality_check_list_detail_item_image_2);

        QualityCheckListDetailInspectionInfo info = mBean.inspectionInfo;

        //头像

        ImageLoader.showHeadIcon(mActivity,R.drawable.icon_default_boy,headIcon);
        //创建者
        nameView.setText(info.creatorName+ (TextUtils.isEmpty(info.inspectionUserTitle)?"":"-"+info.inspectionUserTitle));
        //状态
        statusView.setBackgroundResource(R.drawable.check_list_status_blue);
        statusView.setTextColor(mActivity.getResources().getColor(R.color.c_00b5f2));
        statusView.setText("inspection".equals(info.inspectionType)?"检查":"验收");
        //时间
        timeView.setText(DateUtil.getNormalTime(Long.parseLong(info.updateTime)));
        //描述，无
        desView.setVisibility(View.GONE);

        //整改期限
        timeLimitView.setText("整改期："+DateUtil.getNormalDate(Long.parseLong(info.lastRectificationDate)));
        //图片
        List<QualityCheckListBeanItemFile> files  = mBean.inspectionInfo.files;
        if(files!=null && files.size()!=0){
            int size = files.size();
            if(size == 1){
                oneImageView.setVisibility(View.VISIBLE);
                imageParent.setVisibility(View.GONE);
                ImageLoader.showImageNormal(mActivity,files.get(0).url,oneImageView);
            }else{
                int w = (ScreenUtil.getScreenInfo()[0]-ScreenUtil.dp2px(56))/3;
                image0.setVisibility(View.VISIBLE);
                image0.getLayoutParams().width = w;
                image0.getLayoutParams().height = w;
                image1.setVisibility(View.VISIBLE);
                image1.getLayoutParams().width = w;
                image1.getLayoutParams().height = w;
                ImageLoader.showImageNormal(mActivity,files.get(0).url,image0);
                ImageLoader.showImageNormal(mActivity,files.get(1).url,image1);
                if(size == 2){
                    image2.setVisibility(View.GONE);
                }else{
                    image2.setVisibility(View.VISIBLE);
                    image2.getLayoutParams().width = w;
                    image2.getLayoutParams().height = w;
                    ImageLoader.showImageNormal(mActivity,files.get(1).url,image2);
                }
            }
        }else{
            oneImageView.setVisibility(View.GONE);
            imageParent.setVisibility(View.GONE);
        }

    }

    //添加整改复查记录
    private void addHistoryDescription(QualityCheckListDetailProgressInfo info){
        View view = LayoutInflater.from(mActivity).inflate(R.layout.quality_check_list_detail_item_view,null);
        mParent.addView(view);
        ImageView headIcon = view.findViewById(R.id.quality_check_list_detail_item_head);
        TextView nameView = view.findViewById(R.id.quality_check_list_detail_item_name);
        TextView timeView = view.findViewById(R.id.quality_check_list_detail_item_time);
        TextView statusView = view.findViewById(R.id.quality_check_list_detail_item_status);
        TextView desView = view.findViewById(R.id.quality_check_list_detail_item_text);
        TextView timeLimitView = view.findViewById(R.id.quality_check_list_detail_item_time_limit);
        ImageView oneImageView = view.findViewById(R.id.quality_check_list_detail_item_image_one);
        LinearLayout imageParent = view.findViewById(R.id.quality_check_list_detail_item_image_parent);
        ImageView image0 = view.findViewById(R.id.quality_check_list_detail_item_image_0);
        ImageView image1 = view.findViewById(R.id.quality_check_list_detail_item_image_1);
        ImageView image2 = view.findViewById(R.id.quality_check_list_detail_item_image_2);

        //头像
        ImageLoader.showHeadIcon(mActivity,"",headIcon);
        //创建者
        nameView.setText(info.handlerName+(TextUtils.isEmpty(info.handlerTitle)?"":"-"+info.handlerTitle));
        //最新时间
        timeView.setText(DateUtil.getNormalTime(Long.parseLong(info.updateTime)));
        //状态
        String billType = info.billType;
        statusView.setText(billType);
        if("整改".equals(info.billType)){
            statusView.setBackgroundResource(R.drawable.check_list_status_orange);
            statusView.setTextColor(mActivity.getResources().getColor(R.color.c_f6ad5f));
            timeLimitView.setVisibility(View.GONE);
        }else if("复查".equals(info.billType)){
            statusView.setBackgroundResource(R.drawable.check_list_status_red);
            statusView.setTextColor(mActivity.getResources().getColor(R.color.c_f33d3d));
            timeLimitView.setVisibility(View.VISIBLE);
            //整改期限
            timeLimitView.setText("整改期："+DateUtil.getNormalDate(Long.parseLong(info.lastRectificationDate)));
        }
        //描述
        desView.setText(info.description);
        //图片
        List<QualityCheckListBeanItemFile> files  = mBean.inspectionInfo.files;
        if(files!=null && files.size()!=0){
            int size = files.size();
            if(size == 1){
                oneImageView.setVisibility(View.VISIBLE);
                imageParent.setVisibility(View.GONE);
                ImageLoader.showImageNormal(mActivity,files.get(0).url,oneImageView);
            }else{
                int w = (ScreenUtil.getScreenInfo()[0]-ScreenUtil.dp2px(56))/3;
                image0.setVisibility(View.VISIBLE);
                image0.getLayoutParams().width = w;
                image0.getLayoutParams().height = w;
                image1.setVisibility(View.VISIBLE);
                image1.getLayoutParams().width = w;
                image1.getLayoutParams().height = w;
                ImageLoader.showImageNormal(mActivity,files.get(0).url,image0);
                ImageLoader.showImageNormal(mActivity,files.get(1).url,image1);
                if(size == 2){
                    image2.setVisibility(View.GONE);
                }else{
                    image2.setVisibility(View.VISIBLE);
                    image2.getLayoutParams().width = w;
                    image2.getLayoutParams().height = w;
                    ImageLoader.showImageNormal(mActivity,files.get(1).url,image2);
                }
            }
        }else{
            oneImageView.setVisibility(View.GONE);
            imageParent.setVisibility(View.GONE);
        }

    }


    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialogManager(getActivity());
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    public QualityCheckListDetailBean getDetailInfo() {
        return mBean;
    }
}
