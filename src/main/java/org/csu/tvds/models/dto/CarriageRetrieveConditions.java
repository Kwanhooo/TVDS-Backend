package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class CarriageRetrieveConditions {
    // 日期范围（compositeTime）
    private String startDate;
    private String endDate;
}
