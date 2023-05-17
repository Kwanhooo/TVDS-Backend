package org.csu.tvds.models.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.csu.tvds.entity.mysql.DefectInfo;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefectOverviewVO extends DefectInfo {
    private Integer cameraNo;
    private Integer carriageId;
}
