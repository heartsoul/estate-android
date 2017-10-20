package com.glodon.bim.business.main.bean;

import java.io.Serializable;

/**
 * 描述：
 * 作者：zhourf on 2017/10/18
 * 邮箱：zhourf@glodon.com
 */

public class ProjectListItem implements Serializable{
    public int actualDuration;
    public String actualEnd;
    public String actualStart;
    public String address;
    public ProjectListItemAttachmentInfo attachmentInfo;
    public String code;
    public String countryCode;
    public long deptId;
    public String description;
    public long id;
    public String name;
    public long parentDeptId;
    public String parentDeptName;
    public long plannedDuration;
    public String plannedEnd;
    public String plannedStart;
    public String projectStatusCode;
    public String projectStatusName;
    public String projectTypeCode;
    public String projectTypeName;
    public String regionCode;
    public String regionName;
    public String responder;
    public String scale;
    public String simpleName;
}
