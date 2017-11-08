package com.glodon.bim.business.authority;

import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;

/**
 * 描述：权限返回值
 * 作者：zhourf on 2017/11/8
 * 邮箱：zhourf@glodon.com
 */

public class AuthorityBean {
    public String moduleCode;
    public List<String> actionRights;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String str :actionRights){
            sb.append(str+"  ");
        }
        return "moduleCode="+moduleCode+"  actionRights= "+sb.toString();
    }
}
