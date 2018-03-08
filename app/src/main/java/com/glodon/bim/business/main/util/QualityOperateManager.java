package com.glodon.bim.business.main.util;

import android.view.View;

import com.glodon.bim.base.IBaseView;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.view.SaveDeleteDialog;
import com.glodon.bim.customview.ToastManager;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by cwj on 2018/3/8.
 * Description:质检清单操作管理
 */

public class QualityOperateManager {
    private IBaseView mView;
    private CompositeSubscription mSubscription;

    public QualityOperateManager(IBaseView mView) {
        this.mView = mView;
        mSubscription = new CompositeSubscription();
    }

    /**
     * 删除质检清单
     * @param QualityCheckListBeanItem
     */
    public void deleteQuality(final QualityCheckListBeanItem QualityCheckListBeanItem){
        new SaveDeleteDialog(mView.getActivity())
                .getDeleteDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                            CreateCheckListModel model = new CreateCheckListModel();
                            model.setInspectionType(QualityCheckListBeanItem.inspectionType);
                            Subscription sub = model.createDelete(SharedPreferencesUtil.getProjectId(), QualityCheckListBeanItem.id)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<ResponseBody>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            LogUtil.e(e.getMessage());

                                        }

                                        @Override
                                        public void onNext(ResponseBody responseBody) {

                                            if (responseBody != null) {
//                                                mQualityList.remove(position);
//                                                mView.showResult(mQualityList, mEquipmentList);
                                            }
                                        }
                                    });
                            mSubscription.add(sub);
                        } else {
                            ToastManager.showNetWorkToast();
                        }
                    }
                }).show();
    }

}
