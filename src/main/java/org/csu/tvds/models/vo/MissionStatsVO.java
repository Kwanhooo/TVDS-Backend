package org.csu.tvds.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MissionStatsVO {
    private long uid;
    private long dbId;
    private int status;
    private String model;
    private String inspection;
    private int carriageNo;
    private DefectBriefVO defectBrief;
    private LocalDateTime time;
    private String type;
}
