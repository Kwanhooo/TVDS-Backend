package org.csu.tvds.models.vo;

import lombok.Data;

@Data
public class VisionResultVO {
    boolean isSucceed;
    String message;
    Object data;
}
