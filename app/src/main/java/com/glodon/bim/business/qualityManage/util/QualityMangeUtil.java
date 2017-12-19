package com.glodon.bim.business.qualityManage.util;

import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailInspectionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质量业务工具
 * 作者：zhourf on 2017/12/19
 * 邮箱：zhourf@glodon.com
 */

public class QualityMangeUtil {

    /**
     * 将详情转为参数
     */
    public static CreateCheckListParams getProps(QualityCheckListDetailInspectionInfo info){
        CreateCheckListParams props = new CreateCheckListParams();
        props.inspectId = info.id;
        props.responsibleUserTitle = info.responsibleUserTitle;
        props.code = info.code;
        props.inspectionType = info.inspectionType;
        List<CreateCheckListParamsFile> fileList = new ArrayList<>();
        for(QualityCheckListBeanItemFile file:info.files){
            CreateCheckListParamsFile f = new CreateCheckListParamsFile();

            f.extData = file.extData;
            f.objectId = file.objectId;
            f.name = file.name;
            f.url = file.url;
            fileList.add(f);
        }
        if(fileList.size()>0){
            props.files = fileList;
        }
        props.buildingId = info.buildingId;
        props.buildingName = info.buildingName;
        props.constructionCompanyId = info.constructionCompanyId;
        props.constructionCompanyName = info.constructionCompanyName;
        props.inspectionCompanyId = info.inspectionCompanyId;
        props.inspectionCompanyName = info.inspectionCompanyName;
        props.description = info.description;
        props.elementId = info.elementId;
        props.elementName = info.elementName;
        props.lastRectificationDate = info.lastRectificationDate+"";
        props.needRectification = info.needRectification;
        props.projectId  = info.projectId;
        props.projectName = info.projectName;
        props.qualityCheckpointId = info.qualityCheckpointId;
        props.qualityCheckpointName = info.qualityCheckpointName;
        props.responsibleUserName = info.responsibleUserName;
        props.responsibleUserId = info.responsibleUserId;
        props.gdocFileId = info.gdocFileId;
        return  props;
    }
}
