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

    public static final String[] CLASSIFY_NAMES = {"全部","待提交","待整改","待复查","已检查","已复查","已延迟","已验收"};
    public static final String[] CLASSIFY_STATES = {"","staged","unrectified","unreviewed","inspected","reviewed","delayed","accepted"};
}
