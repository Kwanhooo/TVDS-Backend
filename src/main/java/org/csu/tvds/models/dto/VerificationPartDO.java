package org.csu.tvds.models.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VerificationPartDO {
    private Long partId;
    private Integer status;
}
