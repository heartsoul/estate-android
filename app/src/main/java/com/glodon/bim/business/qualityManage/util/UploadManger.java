package com.glodon.bim.business.qualityManage.util;

import android.content.Context;
import android.os.SystemClock;
import android.widget.ImageView;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.UploadImageBean;
import com.glodon.bim.business.qualityManage.bean.UploadImageBeanData;
import com.glodon.bim.business.qualityManage.listener.OnUploadImageListener;
import com.glodon.bim.business.qualityManage.model.UploadImageApi;
import com.glodon.bim.customview.album.TNBImageItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 描述：图片上传
 * 作者：zhourf on 2017/11/9
 * 邮箱：zhourf@glodon.com
 */

public class UploadManger {
    private LinkedHashMap<String ,TNBImageItem> mSelectedMap;
    private OnUploadImageListener mListener;
    private List<CreateCheckListParamsFile> mDataList;
    private Map<String,CreateCheckListParamsFile> mResult;
    private int count = 0;
    public UploadManger(LinkedHashMap<String ,TNBImageItem> selectedMap){
        this.mSelectedMap = selectedMap;
        mDataList = new ArrayList<>();
        mResult = new HashMap<>();
    }

    public void uploadImages(OnUploadImageListener listener){
        this.mListener = listener;
        for (Map.Entry<String, TNBImageItem> entry : mSelectedMap.entrySet()) {
           String imagePath = entry.getValue().imagePath;
            if(!entry.getValue().objectId.equals("-1")){
                mResult.put(imagePath,entry.getValue().urlFile);
                count++;
                if(count == mSelectedMap.size()){
                    for (Map.Entry<String, TNBImageItem> entry2 : mSelectedMap.entrySet()) {
                        String imagePath2 = entry2.getValue().imagePath;
                        mDataList.add(mResult.get(imagePath2));
                    }
                    if (mListener != null) {
                        mListener.onUploadFinished(mDataList);
                    }
                }
            }else {
                getOperationCode(imagePath);
            }
        }
    }

    private void getOperationCode(final String imagePath) {
        final File file = new File(imagePath);
        String containerId = SystemClock.currentThreadTimeMillis() + "";
        final String name = file.getName();
        String digest = name;
        long length = file.length();
        //http://172.16.233.183:8093/v1/operationCodes
        NetRequest.getInstance().getCall(AppConfig.BASE_URL, UploadImageApi.class).getOperationCode(containerId,name,digest,length,new DaoProvider().getCookie())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String operationCode = null;
                        try {
                            if(response.body()!=null) {
                                operationCode = response.body().string();
                                LogUtil.e("code = ", operationCode);
                                uploadImage(operationCode, file,imagePath);
                            }else if(response.errorBody()!=null){
                                LogUtil.e("erro="+response.errorBody().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if(mListener!=null){
                            mListener.onUploadError(t);
                        }
                    }
                });
    }


    private void uploadImage(String operationCode, File file, final String imagePath) {
        UploadImageApi service = NetRequest.getInstance().getCall(AppConfig.BASE_UPLOAD_URL, UploadImageApi.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
        String temp = "v1/insecure/objects?operationCode=" + operationCode;//使用ip时  用这个
//        String temp = "nss/v1/insecure/objects?operationCode=" + operationCode;//使用域名时用这个
        Call<UploadImageBean> resultCall = service.uploadImage2(temp, body);

        resultCall.enqueue(new Callback<UploadImageBean>() {
            @Override
            public void onResponse(Call<UploadImageBean> call, Response<UploadImageBean> response) {
                count++;
                if(response.errorBody()!=null){
                    try {
                        LogUtil.e("error="+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(response!=null && response.body()!=null && "success".equals(response.body().message)){
                    UploadImageBeanData data = response.body().data;
                    CreateCheckListParamsFile param = new CreateCheckListParamsFile();
                    param.name = data.name;
                    param.objectId = data.id;
                    param.extension = data.extension;
                    param.digest = data.digest;
                    param.length = data.length;
                    param.uploadTime = data.createTime+"";
                    mResult.put(imagePath,param);
                    LogUtil.e("上传成功="+imagePath);
                }
                if(count == mSelectedMap.size()) {
                    if (mResult.size() == mSelectedMap.size()) {
                        for (Map.Entry<String, TNBImageItem> entry : mSelectedMap.entrySet()) {
                            String imagePath = entry.getValue().imagePath;
                            mDataList.add(mResult.get(imagePath));
                        }
                        if (mListener != null) {
                            mListener.onUploadFinished(mDataList);
                        }
                    }else{
                        if(mListener!=null){
                            mListener.onUploadError(new Throwable("error"));
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<UploadImageBean> call, Throwable t) {
                if(mListener!=null){
                    mListener.onUploadError(t);
                }
            }
        });
    }

    public static void loadOriginalUrl(final Context context, String objectId, final ImageView view){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL,UploadImageApi.class).getOriginalUrl(objectId,false,new DaoProvider().getCookie())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String url = response.body().string();
                            ImageLoader.showPreviewImage(context,url,view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                    }
                });
    }
}
