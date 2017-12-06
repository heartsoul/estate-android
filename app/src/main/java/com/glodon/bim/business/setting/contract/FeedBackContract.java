package com.glodon.bim.business.setting.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.setting.bean.FeedBackBean;
import com.glodon.bim.business.setting.bean.FeedBackParams;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;

/**
 * 描述：意见反馈
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface FeedBackContract {

    interface Presenter extends IBasePresenter {


        void addFeedBack(FeedBackParams mParams);
    }

    interface View extends IBaseView {


    }

    interface Model {
        Observable<FeedBackBean> addFeedBack(FeedBackParams props);
    }
}
