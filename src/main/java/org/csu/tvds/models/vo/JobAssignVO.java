package org.csu.tvds.models.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.csu.tvds.entity.mysql.JobAssign;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobAssignVO extends JobAssign {
    private String missionId;
    private String id;
    private Integer inspectionSeq;
    private Integer cameraNumber;
    private Integer carriageId;
    private Integer carriageNo;
    private String model;
    private Integer status;
    private Boolean hasDefect;
    private String comment;
    private String compositeUrl;
    private LocalDateTime compositeTime;
    private String alignedUrl;
    private LocalDateTime alignTime;
    private String url;
    private List<PartCountVO> partCount;
}
