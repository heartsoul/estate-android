package com.glodon.bim.basic.network;

import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 描述：离线处理
 * 作者：zhourf on 2018/4/3
 * 邮箱：zhourf@glodon.com
 */

public class OfflineManager {

    public static String getLocalData(Request request){
        String result = "";
        String path = request.url().encodedPath();
        switch (path){
            case "pmbasic/projects/available":
//                result = xxxHandler.getProjects();
                break;
        }
        return result;
    }

    private static String getMsg(){
        ProjectListBean bean = new ProjectListBean();
        bean.totalPages = 4;
        bean.size = 20;
        List<ProjectListItem> list = new ArrayList<>();
        ProjectListItem item0 = new ProjectListItem();
        item0.name = "item0";
        item0.id = 100;
        item0.deptId = 100;
        list.add(item0);
        ProjectListItem item1 = new ProjectListItem();
        item1.name = "item1";
        item1.id = 101;
        item1.deptId = 101;
        list.add(item1);
        bean.content = list;
        return new GsonBuilder().create().toJson(bean);
    }
}
