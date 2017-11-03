package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailProgressInfo;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailViewContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailViewPresenter;

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

    public QualityCheckListDetailView(Activity mActivity, View view) {
        this.mActivity = mActivity;
        this.view = view;
        initView();
    }


    private  void initView() {
        mParent = (LinearLayout) view.findViewById(R.id.quality_check_list_detail_parent);
        mModuleBenchMarkView = (ImageView) view.findViewById(R.id.quality_check_list_detail_benchmark);
        mCompanyView = (TextView) view.findViewById(R.id.quality_check_list_detail_company);
        mPersonView = (TextView) view.findViewById(R.id.quality_check_list_detail_person);
        mModuleView = (TextView) view.findViewById(R.id.quality_check_list_detail_module);
        mSiteDescription = (TextView) view.findViewById(R.id.quality_check_list_detail_site_description);
        mSaveTimeView = (TextView) view.findViewById(R.id.quality_check_list_detail_time_save);
        mSubmitTimeView = (TextView) view.findViewById(R.id.quality_check_list_detail_time_submit);
        mBluePrintView = (TextView) view.findViewById(R.id.quality_check_list_detail_blueprint);
        mModelView = (TextView) view.findViewById(R.id.quality_check_list_detail_model);
    }

    public void getInfo(long deptId,long id){
        mPresenter = new QualityCheckListDetailViewPresenter(this);
        mPresenter.getInspectInfo(deptId,id);
    }

    public void updateData(QualityCheckListDetailBean bean) {
        mBean = bean;
        addCheckDescription();
        List<QualityCheckListDetailProgressInfo> list = mBean.progressInfos;
        if(list!=null && list.size()>0){
            for(QualityCheckListDetailProgressInfo info :list){
                addHistoryDescription(info);
            }
        }
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

        statusView.setBackgroundResource(R.drawable.check_list_status_blue);
        statusView.setTextColor(mActivity.getResources().getColor(R.color.c_00b5f2));
        desView.setVisibility(View.GONE);

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

        ImageLoader.showHeadIcon(mActivity,"",headIcon);
        nameView.setText(info.handlerName);
        timeView.setText(info.updateTime);

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
            timeLimitView.setText(info.lastRectificationDate);
        }

        desView.setText(info.description);

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

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }
}
