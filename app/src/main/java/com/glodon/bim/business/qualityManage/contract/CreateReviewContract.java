package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.customview.album.TNBImageItem;

import java.util.LinkedHashMap;

/**
 * 描述：新建复查单  整改单
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface CreateReviewContract {

    interface Presenter extends IBasePresenter {


        void setSelectedImages(LinkedHashMap<String, TNBImageItem> map);

        void takePhoto();

        void openAlbum();
    }

    interface View extends IBaseView {
        void showImages(LinkedHashMap<String, TNBImageItem> mSelectedMap);
    }

    interface Model {
    }
}
