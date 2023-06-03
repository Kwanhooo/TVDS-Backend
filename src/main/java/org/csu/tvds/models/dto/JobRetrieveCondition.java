package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class JobRetrieveCondition {
    private String dateBegin;
    private String dateEnd;
    private String inspectionSeq;
    private String carriageId;
    private String imageId;
    private String model;
    private String cameraNumber;
}
