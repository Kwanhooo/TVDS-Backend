package org.csu.tvds.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MissionStatsVO {
    private long uid;
    private int status;
    private int inspection;
    private int carriageNo;
    private String type;
}
