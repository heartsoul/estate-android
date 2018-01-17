package com.glodon.bim.business.main.bean;

import android.view.View;

/**
 * 描述：目录具体功能
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseCategoryItem {
    public int resource;
    public String name;
    public View.OnClickListener listener;

    public ChooseCategoryItem(int resource, String name, View.OnClickListener listener) {
        this.resource = resource;
        this.name = name;
        this.listener = listener;
    }
}
