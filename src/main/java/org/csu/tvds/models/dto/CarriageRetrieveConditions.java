package org.csu.tvds.models.dto;

import lombok.Data;

@Data
public class CarriageRetrieveConditions {
    //    private List<String> treeList;
    private String dateBegin;
    private String dateEnd;
    private String inspectionSeq;
    private String carriageId;
    private String imageId;
    private String model;
    private String cameraNumber;
}
