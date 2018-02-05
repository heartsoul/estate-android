package com.glodon.bim.business.equipment.bean;

import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * 描述：
 * 作者：zhourf on 2018/1/18
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentParams {

    public String code;//单据编号
    public long projectId;//项目id
    public String projectName;//项目名称


    //必填项
    public long acceptanceCompanyId;//验收单位
    public String acceptanceCompanyName;//验收单位
    public String batchCode;//批次编号
    public String facilityCode;//材设编码
    public String facilityName;//材设名称
    public String approachDate;//进场日期

    //非必填项
    public String quantity;//进场数量
    public String unit;//单位
    public String specification;//规格
    public String modelNum;//型号
    public String manufacturer;//厂家
    public String brand;//品牌
    public String supplier;//供应商
    //模型
    public String versionId;//模型版本id
    public String gdocFileId;//模型的文件id
    public Long buildingId;//单体id
    public String buildingName;//单体name
    public String elementId;//构件id
    public String elementName;//构件name
    //    public String modelId;//

    //图片
    public List<CreateCheckListParamsFile> files;//图片
    public boolean qualified;//是否合格


    @Override
    public String toString() {
        return "CreateEquipmentParams{" +
                "code='" + code + '\'' +
                "acceptanceCompanyId='" + acceptanceCompanyId + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", facilityCode='" + facilityCode + '\'' +
                ", facilityName='" + facilityName + '\'' +
                ", approachDate='" + approachDate + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", specification='" + specification + '\'' +
                ", modelNum='" + modelNum + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", brand='" + brand + '\'' +
                ", supplier='" + supplier + '\'' +
                ", versionId='" + versionId + '\'' +
                ", gdocFileId='" + gdocFileId + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", elementId='" + elementId + '\'' +
                ", elementName='" + elementName + '\'' +
//                ", files='" + new GsonBuilder().create().toJson(files) + '\'' +
                ", qualified=" + qualified +
                '}';
    }
}
