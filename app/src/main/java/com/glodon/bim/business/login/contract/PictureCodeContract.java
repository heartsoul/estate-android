package com.glodon.bim.business.login.contract;

import android.graphics.Bitmap;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.login.bean.CheckAccountBean;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：获取图片验证码
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface PictureCodeContract {

    interface Presenter extends IBasePresenter {
        /**
         * 获取图片验证码
         */
         void getPictureCode();

        /**
         * 验证账户
         * @param account  账户
         * @param code  图片码
         */
        void checkAccount(String account, String code);
    }

    interface View extends IBaseView {


        /**
         * 展示图片验证码
         * @param bm
         */
        void showPictureCode(Bitmap bm);
    }

    interface Model {
        /**
         * 验证用户是否存在
         */
        Observable<CheckAccountBean> checkAccount(String identity);

        /**
         * 获取手机验证码
         */
        Observable<CheckAccountBean> getPhoneCode(String mobile,String captcha,String signupKey);
    }
}
