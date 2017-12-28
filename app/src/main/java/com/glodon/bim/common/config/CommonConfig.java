package com.glodon.bim.common.config;

/**
 * 描述：业务配置
 * 作者：zhourf on 2017/10/20
 * 邮箱：zhourf@glodon.com
 */

public class CommonConfig {
    public static final String PROJECT_LIST_ITEM = "ProjectListItem";
    public static final String IMAGE_PATH = "imagePath";
    public static final String IAMGE_SAVE_PATH = "imageSavePath";
    public static final String MAIN_FROM_TYPE = "mainFromType";
    public static final String CHANGE_PROJECT = "changeProject";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MODULE_LIST_POSITION = "moduleListPosition";
    public static final String MODULE_LIST_NAME = "moduleListName";
    public static final String TYPE_INSPECTION = "inspection";
    public static final String TYPE_ACCEPTANCE = "acceptance";
    public static final String ALBUM_DATA = "albumData";
    public static final String ALBUM_FROM_TYPE = "albumFromType";
    public static final String SHOW_PHOTO = "showPhoto";
    public static final String FROM_CREATE_CHECK_LIST = "fromCheckList";
    public static final String ALBUM_POSITION = "albumPosition";
    public static final String QUALITY_CHECK_LIST_DEPTID = "qualityCheckListDeptId";
    public static final String QUALITY_CHECK_LIST_ID = "qualityCheckListId";
    public static final String CREATE_TYPE = "createType";
    public static final String CREATE_TYPE_CHECK = "0";//新建检查单
    public static final String CREATE_TYPE_REPAIR = "1";//新建整改单
    public static final String CREATE_TYPE_REVIEW = "2";//新建复查单

    public static final String[] CLASSIFY_NAMES = {"全部","待提交",  "待整改",      "待复查",    "已检查",    "已复查",  "已延迟",  "已验收"};
    public static final String[] CLASSIFY_STATES = {"",   "staged",  "unrectified","unreviewed","inspected","reviewed","delayed","accepted"};
    /**
     * 复查合格 "closed"
     *
     * 复查不合格  "notAccepted"
     */
    public static final String STATUS_CLOSED = "closed";
    public static final String STATUS_NOT_ACCEPTED = "notAccepted";
    public static final String QC_STATE_STAGED = "staged";
    public static final String QC_STATE_UNRECTIFIED = "unrectified";
    public static final String QC_STATE_UNREVIEWED = "unreviewed";
    public static final String QC_STATE_INSPECTED = "inspected";
    public static final String QC_STATE_REVIEWED = "reviewed";
    public static final String QC_STATE_DELAYED = "delayed";
    public static final String QC_STATE_ACCEPTED = "accepted";
    public static final String CREATE_CHECK_LIST_PROPS = "createCheckListProps";
    public static final String QUALITY_CHECK_LIST_SHOW_REPAIR = "showRepair";


    /**
     * 广播
     */
    public static final String ACTION_GET_AUTHORITY_CHECK = "com.glodon.bim.action.authority.check";//权限获取完毕
    public static final String ACTION_BRUSH_CHECK_LIST = "com.glodon.bim.action.brush.check.list";
    public static final String ALBUM_SHOW_DELETE = "albumShowDelete";
    public static final String MODULE_TEMPLATEID = "module_templateid";
    public static final String ACTION_LOG_OUT = "com.glodon.bim.action.logout";//退出登录发送广播
    public static final String BLUE_PRINT_FILE_ID = "BLUE_PRINT_FILE_ID";//图纸的id
    public static final String BLUE_PRINT_FILE_NAME = "BLUE_PRINT_FILE_NAME";//图纸的NAME
    public static final String BLUE_PRINT_POSITION_X = "BLUE_PRINT_POSITION_X";//图纸位置x
    public static final String BLUE_PRINT_POSITION_Y = "BLUE_PRINT_POSITION_Y";//图纸位置y
    public static final String BLUE_PRINT_FILE = "BLUE_PRINT_FILE";
    public static final String MODEL_SELECT_INFO = "MODEL_SELECT_INFO";//关联模型
    public static final String RELEVANT_TYPE = "RELEVANT_TYPE";//传递的类型  //0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
    public static final String DRAWINGGDOCFILEID = "DRAWINGGDOCFILEID";//从图纸模式进入检查单
    public static final String DRAWINGNAME = "DRAWINGNAME";//从图纸模式进入检查单
    public static final String DRAWINGPOSITIONX = "DRAWINGPOSITIONX";//从图纸模式进入检查单
    public static final String DRAWINGPOSITIONY = "DRAWINGPOSITIONY";//从图纸模式进入检查单
    public static final String RELEVANT_MODEL = "RELEVANT_MODEL";//从模型模式进入检查单
    public static final String SEARCH_TYPE = "SEARCH_TYPE";//搜索类型 0图纸  1模型
    public static final String RELEVANT_TYPE_MODEL = "RELEVANT_TYPE_MODEL";
    public static final String SMSCODE_MOBILE = "SMSCODE_MOBILE";
    public static final String SMSCODE_CODE = "SMSCODE_CODE";
    public static final String RESET_USERNAME = "RESET_USERNAME";
}
