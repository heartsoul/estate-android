package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.contract.MainContract;
import com.glodon.bim.business.main.model.MainModel;
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.setting.bean.CheckVersionBean;
import com.glodon.bim.business.setting.util.GlodonUpdateManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.album.AlbumEditActivity;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：主界面
 * 作者：zhourf on 2018/2/5
 * 邮箱：zhourf@glodon.com
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private MainContract.Model mModel;
    private String mPhotoPath;
    private CompositeSubscription mSubscription;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
        mModel = new MainModel();
        mSubscription = new CompositeSubscription();

    }

    @Override
    public void initData(Intent intent) {
        checkVersion();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO://拍照返回
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH,mPhotoPath);
                    intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_CHECK);//表示创建检查单
                    mView.getActivity().startActivity(intent);

                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mModel = null;
        mView = null;
    }

    @Override
    public void openPhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE,0);
        intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_CHECK);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void toCreate() {
        Intent intent = new Intent(mView.getActivity(), CreateCheckListActivity.class);
        intent.putExtra(CommonConfig.SHOW_PHOTO,false);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void checkVersion() {
        Subscription sub = mModel.checkVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckVersionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("tag", "onError: ");
                    }

                    @Override
                    public void onNext(CheckVersionBean checkVersionBean) {
                        if (checkVersionBean != null) {
                            SharedPreferencesUtil.saveLatestVersion(checkVersionBean.getVersion());
                            //强制升级 弹窗
                            if ("force".equals(checkVersionBean.getUpdateOption())) {
                                GlodonUpdateManager.getInstance().showForceUpdateDialog(mView.getActivity(),checkVersionBean);
                            } else {
                                //开启wifi下自动下载
                                if (SharedPreferencesUtil.getAutoDownload()) {
                                    GlodonUpdateManager.getInstance().autoDownload(mView.getActivity(),checkVersionBean);
                                }
                            }

                        }
                    }
                });
        mSubscription.add(sub);
    }


}
