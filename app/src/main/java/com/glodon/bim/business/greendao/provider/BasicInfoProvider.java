package com.glodon.bim.business.greendao.provider;

import android.text.TextUtils;

import com.glodon.bim.basic.utils.GsonUtil;
import com.glodon.bim.business.authority.AuthorityInfo;
import com.glodon.bim.business.greendao.entity.BasicInfoEntity;
import com.glodon.bim.business.greendao.handler.BasicInfoHandler;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.common.login.User;
import com.glodon.bim.common.login.UserTenant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 描述：基础数据处理
 * 作者：zhourf on 2018/4/9
 * 邮箱：zhourf@glodon.com
 */

public class BasicInfoProvider {

    private static String TYPE_ACCOUNT = "TYPE_ACCOUNT";
    private static String TYPE_USERINFO = "TYPE_USERINFO";
    private static String TYPE_CURRENT_PHONENUM = "TYPE_CURRENT_PHONENUM";
    private static String TYPE_CURRENT_TENANTID = "TYPE_CURRENT_TENANTID";
    private static String TYPE_CURRENT_PROJECT = "TYPE_CURRENT_PROJECT";
    private static String TYPE_PROJECTS = "TYPE_PROJECTS";
    private static String TYPE_PROJECTS_AUTHORITY = "TYPE_PROJECTS_AUTHORITY";
    private static String TYPE_INSPECTION_COMPANY = "TYPE_INSPECTION_COMPANY";
    private static String TYPE_CONSTURCTION_COMPANY = "TYPE_CONSTURCTION_COMPANY";
    private static String TYPE_RESPONSIBLE_PERSON = "TYPE_RESPONSIBLE_PERSON";
    private static String TYPE_MODULE = "TYPE_MODULE";

    /**
     * 保存账户信息
     */
    public static void insertAccountInfo(String phoneNum,String pwd){
        insert(TYPE_ACCOUNT+phoneNum,phoneNum+pwd);
    }

    /**
     * 核对账户信息
     */
    public static boolean checkAccountInfo(String phoneNum,String pwd){
        BasicInfoEntity entity = BasicInfoHandler.query(TYPE_ACCOUNT+phoneNum);
        return (entity!=null && TextUtils.equals(entity.getValue(),phoneNum+pwd));
    }

    /**
     * 保存当前的用户phonenum
     */
    public static void insertCurrentPhoneNum(String phoneNum){
        insert(TYPE_CURRENT_PHONENUM, phoneNum);
    }
    /**
     * 获取当前的用户的phonenum
     */
    private static String getCurrentPhoneNum(){
        return query(TYPE_CURRENT_PHONENUM);
    }
    /**
     * 保存用户信息
     */
    public static void insertUserInfo(String phoneNum,User user){
        insert(TYPE_USERINFO+phoneNum, GsonUtil.toJsonString(user));
    }

    /**
     * 获取当前的用户信息
     */
    public static User getUserInfo(){
        return query(TYPE_USERINFO+getCurrentPhoneNum());
    }

    /**
     * 获取当前的租户列表
     */
    public List<UserTenant> getCurrentTenants(){
        User user = getUserInfo();
        if(user!=null){
            return user.accountInfo.userTenants;
        }
        return null;
    }

    /**
     * 设置当前选中的租户的id
     */
    public static void insertCurrentTenantId(long tenantid){
        insert(TYPE_CURRENT_TENANTID,tenantid+"");
    }

    /**
     * 获取当前的租户的id
     */
    public static String getCurrentTenantId(){
        return query(TYPE_CURRENT_TENANTID);
    }

    /**
     * 保存租户的项目列表信息
     */
    public static void insertProjects(List<ProjectListItem> list){
        insert(TYPE_PROJECTS+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }

    /**
     * 获取当前租户的项目列表
     */
    public static List<ProjectListItem> getProjects(long tenantId){
        return query(TYPE_PROJECTS+tenantId+getCurrentPhoneNum());
    }

    /**
     * 保存项目的权限信息
     */
    public static void saveProjectAuthority(long deptId, AuthorityInfo info){
        insert(TYPE_PROJECTS_AUTHORITY+deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(info));
    }

    /**
     * 获取项目的权限信息
     */
    public static AuthorityInfo getAuthorityInfo(long deptId){
        return query(TYPE_PROJECTS_AUTHORITY+deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存当前的项目信息
     */
    public static void insertCurrentProjectInfo(ProjectListItem item){
        insert(TYPE_CURRENT_PROJECT,GsonUtil.toJsonString(item));
    }
    /**
     * 获取当前的项目信息
     */
    public static ProjectListItem getCurrentProjectInfo(){
        return query(TYPE_CURRENT_PROJECT);
    }


    /**
     * 保存检查单位信息
     */
    public static void insertInspectionCompany(List<InspectionCompanyItem> list){
        insert(TYPE_INSPECTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取检查单位信息
     */
    public static List<InspectionCompanyItem> getInspectionCompany(){
        return query(TYPE_INSPECTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存施工单位信息
     */
    public static void insertConstructionCompany(List<CompanyItem> list){
        insert(TYPE_CONSTURCTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取施工单位信息
     */
    public static List<CompanyItem> getConstructionCompany(){
        return query(TYPE_CONSTURCTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存责任人信息
     */
    public static void insertResponsiblePerson(long companyId,List<PersonItem> list){
        insert(TYPE_RESPONSIBLE_PERSON+companyId+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取责任人信息
     */
    public static List<PersonItem> getResponsiblePerson(long companyId){
        return query(TYPE_RESPONSIBLE_PERSON+companyId+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }
    /**
     * 保存质检项目信息
     */
    public static void insertModuleInfo(List<ModuleListBeanItem> list){
        insert(TYPE_MODULE+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取质检项目信息
     */
    public static List<PersonItem> getModuleInfo(){
        return query(TYPE_MODULE+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }



    /**
     *
     */
    private static void insert(String key,String value){
        BasicInfoEntity entity = new BasicInfoEntity(null,key, value);
        BasicInfoHandler.insert(entity);
    }

    /**
     * 查询
     */
    private static <T> T query(String key){
        BasicInfoEntity entity = BasicInfoHandler.query(key);
        if(entity!=null){
            return GsonUtil.toJsonObj(entity.getValue(),new TypeToken<T>(){}.getType());
        }
        return null;
    }
}
