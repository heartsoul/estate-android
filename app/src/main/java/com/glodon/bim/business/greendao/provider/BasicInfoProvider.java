package com.glodon.bim.business.greendao.provider;

import android.text.TextUtils;

import com.glodon.bim.basic.utils.GsonUtil;
import com.glodon.bim.business.authority.AuthorityInfo;
import com.glodon.bim.business.greendao.entity.BasicInfoEntity;
import com.glodon.bim.business.greendao.handler.BasicInfoHandler;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.common.login.User;
import com.glodon.bim.common.login.UserTenant;
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
    private static String TYPE_MODEL_SPECIAL = "TYPE_MODEL_SPECIAL";
    private static String TYPE_MODEL_SINGLE = "TYPE_MODEL_SINGLE";
    private static String TYPE_MODEL_LIST = "TYPE_MODEL_LIST";
    private static String TYPE_BLUEPRINT = "TYPE_BLUEPRINT";
    private static String TYPE_CHECK_COMPANY = "TYPE_CHECK_COMPANY";

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
    public static void insertProjects(ProjectListBean list){
        insert(TYPE_PROJECTS+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }

    /**
     * 获取当前租户的项目列表
     * return ProjectListBean
     */
    public static String getProjects(long tenantId){
        return queryString(TYPE_PROJECTS+tenantId+getCurrentPhoneNum());
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
     * return List<InspectionCompanyItem>
     */
    public static String getInspectionCompany(){
        return queryString(TYPE_INSPECTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存施工单位信息
     */
    public static void insertConstructionCompany(List<CompanyItem> list){
        insert(TYPE_CONSTURCTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取施工单位信息
     * return List<CompanyItem>
     */
    public static String getConstructionCompany(){
        return queryString(TYPE_CONSTURCTION_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存责任人信息
     */
    public static void insertResponsiblePerson(long companyId,List<PersonItem> list){
        insert(TYPE_RESPONSIBLE_PERSON+companyId+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取责任人信息
     * return List<PersonItem>
     */
    public static String getResponsiblePerson(long companyId){
        return queryString(TYPE_RESPONSIBLE_PERSON+companyId+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }
    /**
     * 保存质检项目信息
     */
    public static void insertModuleInfo(List<ModuleListBeanItem> list){
        insert(TYPE_MODULE+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取质检项目信息
     * return List<ModuleListBeanItem>
     */
    public static  String getModuleInfo(){
        return queryString(TYPE_MODULE+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存模型专业列表信息
     */
    public static void insertModelSpecial(List<ModelSpecialListItem> list){
        insert(TYPE_MODEL_SPECIAL+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取模型专业列表信息
     * return List<ModelSpecialListItem>
     */
    public static String getModelSpecial(){
        return queryString(TYPE_MODEL_SPECIAL+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存模型单体列表信息
     */
    public static void insertModelSingle(List<ModelSingleListItem> list){
        insert(TYPE_MODEL_SINGLE+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取模型单体列表信息
     * return List<ModelSingleListItem>
     */
    public static String getModelSingle(){
        return queryString(TYPE_MODEL_SINGLE+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存模型列表信息
     */
    public static void insertModelList(List<ModelListBeanItem> list){
        insert(TYPE_MODEL_LIST+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取模型列表信息
     */
    public static List<ModelSingleListItem> getModelList(){
        return query(TYPE_MODEL_LIST+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }

    /**
     * 保存图纸列表信息
     */
    public static void insertBlueprintList(List<BlueprintListBeanItem> list){
        insert(TYPE_BLUEPRINT+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取图纸列表信息
     */
    public static List<BlueprintListBeanItem> getBlueprintList(){
        return query(TYPE_BLUEPRINT+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
    }
    /**
     * 保存材设验收单位信息
     */
    public static void insertCheckCompany(List<InspectionCompanyItem> list){
        insert(TYPE_CHECK_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum(),GsonUtil.toJsonString(list));
    }
    /**
     * 获取材设验收单位信息
     * return List<InspectionCompanyItem>
     */
    public static String getCheckCompany(){
        return queryString(TYPE_CHECK_COMPANY+getCurrentProjectInfo().deptId+getCurrentTenantId()+getCurrentPhoneNum());
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

    /**
     * 查询
     */
    private static String queryString(String key){
        BasicInfoEntity entity = BasicInfoHandler.query(key);
        if(entity!=null){
            return entity.getValue();
        }
        return null;
    }
}
