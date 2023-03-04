package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class OriginRetrieveConditions {
    // 日期范围（createTime）
    private String startDate;
    private String endDate;
}
