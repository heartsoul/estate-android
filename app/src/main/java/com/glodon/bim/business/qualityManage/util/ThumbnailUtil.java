package com.glodon.bim.business.qualityManage.util;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ThumbnailBean;
import com.glodon.bim.business.qualityManage.model.ThumbnailApi;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 描述：获取缩略图
 * 作者：zhourf on 2017/12/22
 * 邮箱：zhourf@glodon.com
 */

public class ThumbnailUtil {

    /**
     * 获取缩略图并展示
     * @param fileId  图纸或模型id
     * @param view  展示的view
     */
    public static void getThumbnail(final Activity mActivity, long projectId, String projectVersionId, String fileId, final ImageView view,final int defaultIcon){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL, ThumbnailApi.class).getBluePrint(projectId,projectVersionId,fileId,new DaoProvider().getCookie())
                .enqueue(new Callback<ThumbnailBean>() {
                    @Override
                    public void onResponse(Call<ThumbnailBean> call, Response<ThumbnailBean> response) {
                        if(response!=null){
//                            LogUtil.e("body = "+new GsonBuilder().create().toJson(response.body()));
                            if(response.body()!=null) {
                                ThumbnailBean bean = response.body();
                                String url = "";
                                if(bean.data!=null&&!TextUtils.isEmpty(bean.data.thumbnailUrl)){
                                    url = bean.data.thumbnailUrl;
                                }
                                ImageLoader.showImageCenterCrop(mActivity,url,view, defaultIcon);

                            }

                            if(response.errorBody()!=null){
                                try {
                                    LogUtil.e("getthumbnail error = "+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ThumbnailBean> call, Throwable t) {

                    }
                });
    }
}
