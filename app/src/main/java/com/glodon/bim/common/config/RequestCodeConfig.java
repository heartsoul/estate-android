package com.glodon.bim.common.config;

/**
 * 描述：所有的requestcode
 * 作者：zhourf on 2017/12/25
 * 邮箱：zhourf@glodon.com
 */

public class RequestCodeConfig {

    public static final int REQUEST_CODE_TO_RELEVANT_BLUEPRINT = 0;
    public static final int REQUEST_CODE_OPEN_MODEL = REQUEST_CODE_TO_RELEVANT_BLUEPRINT+1;
    public static final int REQUEST_CODE_TO_RELEVANT = REQUEST_CODE_OPEN_MODEL+1;
    public static final int REQUEST_CODE_TO_SEARCH = REQUEST_CODE_TO_RELEVANT+1;
    public static final int REQUEST_CODE_CHOOSE_MODULE = REQUEST_CODE_TO_SEARCH+1;//跳转到选择质检项目
    public static final int REQUEST_CODE_OPEN_ALBUM = REQUEST_CODE_CHOOSE_MODULE+1;
    public static final int REQUEST_CODE_TAKE_PHOTO = REQUEST_CODE_OPEN_ALBUM+1;
    public static final int REQUEST_CODE_PHOTO_EDIT = REQUEST_CODE_TAKE_PHOTO+1;
    public static final int REQUEST_CODE_PHOTO_PREVIEW = REQUEST_CODE_PHOTO_EDIT+1;//图片预览
    public static final int REQUEST_CODE_CHOOSE_BLUE_PRINT = REQUEST_CODE_PHOTO_PREVIEW+1;//跳转到选择图纸
    public static final int REQUEST_CODE_CHOOSE_MODEL = REQUEST_CODE_CHOOSE_BLUE_PRINT+1;//跳转到选择模型
    public static final int REQUEST_CODE_MODEL_OPEN_MODEL = REQUEST_CODE_CHOOSE_MODEL+1;
    public static final int REQUEST_CODE_MODEL_TO_SEARCH = REQUEST_CODE_MODEL_OPEN_MODEL+1;
    public static final int REQUEST_CODE_CREATE_CHECK_LIST = REQUEST_CODE_MODEL_TO_SEARCH+1;
    public static final int REQUEST_CODE_DETAIL = REQUEST_CODE_CREATE_CHECK_LIST+1;
    public static final int REQUEST_CODE_CREATE_REVIEW = REQUEST_CODE_DETAIL+1;
    public static final int REQUEST_CODE_CREATE_REPAIR = REQUEST_CODE_CREATE_REVIEW+1;
    public static final int REQUEST_CODE_TO_EDIT = REQUEST_CODE_CREATE_REPAIR+1;
    public static final int REQUEST_CODE_CAMERA = REQUEST_CODE_TO_EDIT+1;
    public static final int REQUEST_CODE_CREATE_TYPE_REPAIR = REQUEST_CODE_CAMERA+1;
    public static final int REQUEST_CODE_CREATE_TYPE_REVIEW = REQUEST_CODE_CREATE_TYPE_REPAIR+1;
    public static final int REQUEST_CODE_CHANGE_PROJECT = REQUEST_CODE_CREATE_TYPE_REVIEW+1;
    public static final int REQUEST_CODE_TOSMS = REQUEST_CODE_CHANGE_PROJECT+1;
    public static final int REQUEST_CODE_TORESET = REQUEST_CODE_TOSMS+1;
}
