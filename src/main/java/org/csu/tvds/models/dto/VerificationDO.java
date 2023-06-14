package org.csu.tvds.models.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class VerificationDO {
    // private String comment;
    private List<VerificationPartDO> results;
}
