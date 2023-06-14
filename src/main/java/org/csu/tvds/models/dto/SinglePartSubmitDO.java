package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class SinglePartSubmitDO {
    private Long partId;
    private Integer status;
    private String comment;
}
