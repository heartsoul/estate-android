package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailInspectionInfo;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailProgressInfo;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailViewContract;
import com.glodon.bim.business.qualityManage.listener.OnShowQualityCheckDetailListener;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailViewPresenter;
import com.glodon.bim.business.qualityManage.util.IntentManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.TNBImageItem;
import com.glodon.bim.customview.dialog.LoadingDialogManager;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

import java.util.List;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityCheckListDetailView implements QualityCheckListDetailViewContract.View {

    //检查单问题
    private LinearLayout mParent;
    private TextView mInspectionCompanyView, mCompanyView, mPersonView, mModuleView, mSiteDescription, mSaveTimeView, mSubmitTimeView, mBluePrintView, mModelView;
    private ImageView mModuleBenchMarkView;

    private QualityCheckListDetailBean mBean;

    private Activity mActivity;

    private View view;

    private QualityCheckListDetailViewContract.Presenter mPresenter;

    private LoadingDialogManager mLoadingDialog;

    private OnShowQualityCheckDetailListener mListener;

    private int type = 2;//0新建检查单 1检查单编辑状态 2详情查看  3图纸模式

    public void setmListener(OnShowQualityCheckDetailListener mListener) {
        this.mListener = mListener;
    }

    public QualityCheckListDetailView(Activity mActivity, View view) {
        this.mActivity = mActivity;
        this.view = view;
        initView();
    }


    private void initView() {
        mParent = view.findViewById(R.id.quality_check_list_detail_list);
        mModuleBenchMarkView = view.findViewById(R.id.quality_check_list_detail_benchmark);
        mInspectionCompanyView = view.findViewById(R.id.quality_check_list_detail_inspection_company);
        mCompanyView = view.findViewById(R.id.quality_check_list_detail_company);
        mPersonView = view.findViewById(R.id.quality_check_list_detail_person);
        mModuleView = view.findViewById(R.id.quality_check_list_detail_module);
        mSiteDescription = view.findViewById(R.id.quality_check_list_detail_site_description);
        mSaveTimeView = view.findViewById(R.id.quality_check_list_detail_time_save);
        mSubmitTimeView = view.findViewById(R.id.quality_check_list_detail_time_submit);
        mBluePrintView = view.findViewById(R.id.quality_check_list_detail_blueprint);
        mModelView = view.findViewById(R.id.quality_check_list_detail_model);
    }

    public void getInfo(long deptId, long id) {
        mPresenter = new QualityCheckListDetailViewPresenter(this);
        mPresenter.getInspectInfo(deptId, id);
    }

    public void updateInfo(long deptId, long id) {
        mPresenter.getInspectInfo(deptId, id);
    }

    @Override
    public void updateData(QualityCheckListDetailBean bean) {
        mBean = bean;
        mParent.removeAllViews();
        addBasicInfo();
        addCheckDescription();
        List<QualityCheckListDetailProgressInfo> list = mBean.progressInfos;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                QualityCheckListDetailProgressInfo info = list.get(i);
                addHistoryDescription(info);
            }
        }
        if (mListener != null) {
            mListener.onDetailShow(bean);
        }
    }

    //添加基本信息
    private void addBasicInfo() {
        final QualityCheckListDetailInspectionInfo info = mBean.inspectionInfo;
        mInspectionCompanyView.setText(info.inspectionCompanyName);
        mCompanyView.setText(info.constructionCompanyName);
        mPersonView.setText(info.responsibleUserName);
        mModuleView.setText(info.qualityCheckpointName);
        if (info.qualityCheckpointId != null && info.qualityCheckpointId > 0) {
            mModuleBenchMarkView.setVisibility(View.VISIBLE);
            mModuleBenchMarkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentManager.toModuleStandard(getActivity(), info.qualityCheckpointId.longValue());
                }
            });
        } else {
            mModuleBenchMarkView.setVisibility(View.GONE);
        }
        mSiteDescription.setText(info.description);
        mSaveTimeView.setText(DateUtil.getNormalTime(Long.parseLong(info.createTime)));
        if (info.commitTime > 0) {
            mSubmitTimeView.setText(DateUtil.getNormalTime(info.commitTime));
        }
        mBluePrintView.setText(info.drawingName);
        mBluePrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到图纸
                Intent intent = new Intent(mActivity, RelevantBluePrintActivity.class);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME,info.drawingName);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID,info.drawingGdocFileId);
                intent.putExtra(CommonConfig.RELEVANT_TYPE,type);
                intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_X, info.drawingPositionX);
                intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_Y, info.drawingPositionY);
                mActivity.startActivity(intent);
            }
        });
        mModelView.setText(info.elementName);
        mModelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到模型
                Intent intent = new Intent(mActivity, RelevantModelActivity.class);
                intent.putExtra(CommonConfig.RELEVANT_TYPE, type);
                mActivity.startActivity(intent);
            }
        });
    }

    //添加检查时的问题
    private void addCheckDescription() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.quality_check_list_detail_item_view, null);
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

//        ImageLoader.showHeadIcon(mActivity,R.drawable.icon_default_boy,headIcon);
        //创建者
        LogUtil.e("check title=" + info.inspectionUserTitle);
        nameView.setText(info.creatorName + (TextUtils.isEmpty(info.inspectionUserTitle) ? "" : "-" + info.inspectionUserTitle));
        //状态
        statusView.setBackgroundResource(R.drawable.check_list_status_blue);
        statusView.setTextColor(mActivity.getResources().getColor(R.color.c_00b5f2));
        statusView.setText("inspection".equals(info.inspectionType) ? "检查" : "验收");
        //时间
        if (info.commitTime > 0) {
            timeView.setText(DateUtil.getNormalTime(info.commitTime));
        }
        //描述，无,在上面的信息中已经有描述了
        desView.setVisibility(View.GONE);

        //整改期限
        if (info.lastRectificationDate > 0) {
            timeLimitView.setText("整改期：" + DateUtil.getNormalDate(info.lastRectificationDate));
        }
        //图片
        List<QualityCheckListBeanItemFile> files = info.files;
        if (files != null && files.size() != 0) {
            final LinkedHashList<String, TNBImageItem> mSelectedMap = new LinkedHashList<>();
            int size = files.size();
            for (QualityCheckListBeanItemFile imageFile : files) {
                TNBImageItem imageItem = new TNBImageItem();
                imageItem.imagePath = imageFile.url;
                imageItem.objectId = imageFile.objectId;
                mSelectedMap.put(imageFile.url, imageItem);
            }
            if (size == 1) {
                oneImageView.setVisibility(View.VISIBLE);
                imageParent.setVisibility(View.GONE);
                ImageLoader.showImageNormal(mActivity, files.get(0).url, oneImageView);

                oneImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 0);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
            } else {
                oneImageView.setVisibility(View.GONE);
                imageParent.setVisibility(View.VISIBLE);
                int w = (ScreenUtil.getScreenInfo()[0] - ScreenUtil.dp2px(56)) / 3;
                image0.setVisibility(View.VISIBLE);
                image0.getLayoutParams().width = w;
                image0.getLayoutParams().height = w;
                image1.setVisibility(View.VISIBLE);
                image1.getLayoutParams().width = w;
                image1.getLayoutParams().height = w;
                ImageLoader.showImageNormal(mActivity, files.get(0).url, image0);
                ImageLoader.showImageNormal(mActivity, files.get(1).url, image1);
                if (size == 2) {
                    image2.setVisibility(View.GONE);
                } else {
                    image2.setVisibility(View.VISIBLE);
                    image2.getLayoutParams().width = w;
                    image2.getLayoutParams().height = w;
                    ImageLoader.showImageNormal(mActivity, files.get(2).url, image2);
                }
                image0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 0);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 1);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
                image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 2);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
            }
        } else {
            oneImageView.setVisibility(View.GONE);
            imageParent.setVisibility(View.GONE);
        }

    }

    //添加整改复查记录
    private void addHistoryDescription(QualityCheckListDetailProgressInfo info) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.quality_check_list_detail_item_view, null);
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
//        ImageLoader.showHeadIcon(mActivity,"",headIcon);
        //创建者

        LogUtil.e("history title=" + info.handlerTitle);
        nameView.setText(info.handlerName + (TextUtils.isEmpty(info.handlerTitle) ? "" : "-" + info.handlerTitle));
        //最新时间
        if (info.commitTime > 0) {
            timeView.setText(DateUtil.getNormalTime(info.commitTime));
        }
        //状态
        String billType = info.billType;
        statusView.setText(billType);
        if ("整改".equals(info.billType)) {
            statusView.setBackgroundResource(R.drawable.check_list_status_orange);
            statusView.setTextColor(mActivity.getResources().getColor(R.color.c_f6ad5f));
            timeLimitView.setVisibility(View.GONE);
        } else if ("复查".equals(info.billType)) {
            statusView.setBackgroundResource(R.drawable.check_list_status_red);
            statusView.setTextColor(mActivity.getResources().getColor(R.color.c_f33d3d));
            timeLimitView.setVisibility(View.VISIBLE);
            //整改期限
            if (info.lastRectificationDate != null && Long.parseLong(info.lastRectificationDate) > 0) {
                timeLimitView.setVisibility(View.GONE);
                timeLimitView.setText("整改期：" + DateUtil.getNormalDate(Long.parseLong(info.lastRectificationDate)));
            } else {
                timeLimitView.setVisibility(View.GONE);
            }
        }
        //描述
        desView.setText(info.description);
        //图片
        List<QualityCheckListBeanItemFile> files = info.files;
        if (files != null && files.size() != 0) {
            final LinkedHashList<String, TNBImageItem> mSelectedMap = new LinkedHashList<>();
            int size = files.size();
            for (QualityCheckListBeanItemFile imageFile : files) {
                TNBImageItem imageItem = new TNBImageItem();
                imageItem.imagePath = imageFile.url;
                imageItem.objectId = imageFile.objectId;
                mSelectedMap.put(imageFile.url, imageItem);
            }
            if (size == 1) {
                oneImageView.setVisibility(View.VISIBLE);
                imageParent.setVisibility(View.GONE);
                ImageLoader.showImageNormal(mActivity, files.get(0).url, oneImageView);

                oneImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 0);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
            } else {
                oneImageView.setVisibility(View.GONE);
                imageParent.setVisibility(View.VISIBLE);
                int w = (ScreenUtil.getScreenInfo()[0] - ScreenUtil.dp2px(56)) / 3;
                image0.setVisibility(View.VISIBLE);
                image0.getLayoutParams().width = w;
                image0.getLayoutParams().height = w;
                image1.setVisibility(View.VISIBLE);
                image1.getLayoutParams().width = w;
                image1.getLayoutParams().height = w;
                ImageLoader.showImageNormal(mActivity, files.get(0).url, image0);
                ImageLoader.showImageNormal(mActivity, files.get(1).url, image1);
                if (size == 2) {
                    image2.setVisibility(View.GONE);
                } else {
                    image2.setVisibility(View.VISIBLE);
                    image2.getLayoutParams().width = w;
                    image2.getLayoutParams().height = w;
                    ImageLoader.showImageNormal(mActivity, files.get(2).url, image2);
                }
                image0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 0);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 1);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
                image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
                        intent.putExtra(CommonConfig.ALBUM_POSITION, 2);
                        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
                        getActivity().startActivity(intent);
                    }
                });
            }
        } else {
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
