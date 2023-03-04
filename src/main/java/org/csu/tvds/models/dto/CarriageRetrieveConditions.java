package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class CarriageRetrieveConditions {
    // 日期范围（compositeTime）
    private String startDate;
    private String endDate;

    // 过检号
    private String inspectionSeq;
    // 车厢号
    private String carriageNo;
    // 图片ID
    private String id;
}
