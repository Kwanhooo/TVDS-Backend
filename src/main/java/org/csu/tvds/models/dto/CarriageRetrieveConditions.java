package org.csu.tvds.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class CarriageRetrieveConditions {
    private List<String> treeList;
    private String inspectionSeq;
    private String carriageId;
    private String imageId;
}
