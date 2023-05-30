package org.csu.tvds.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class VerificationDO {
    private String comment;
    private List<VerificationPartDO> results;
}
