package org.csu.tvds.models.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyViewVO extends CarriageOverviewVO {
    List<PartOverviewVO> affiliateParts;
    String personnelSeq;
}
