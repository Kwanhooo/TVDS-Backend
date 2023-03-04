package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class PartRetrieveConditions {
    // 日期范围（createTime）
    private String startDate;
    private String endDate;

    // 过检号
    private String model;
    // 零件类型
    private String partName;
    // 过检号
    private String inspectionSeq;
}
