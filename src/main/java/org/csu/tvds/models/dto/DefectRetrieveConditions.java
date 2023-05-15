package org.csu.tvds.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class DefectRetrieveConditions {
    // 日期范围（createTime）
    private String dateBegin;
    private String dateEnd;

    // 过检号
    private List<String> treeList;
}
