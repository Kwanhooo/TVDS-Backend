package org.csu.tvds.models.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.csu.tvds.entity.mysql.PartInfo;

@EqualsAndHashCode(callSuper = true)
@Data
public class PartOverviewVO extends PartInfo {
    private Integer cameraNo;
    private Integer carriageId;
}
