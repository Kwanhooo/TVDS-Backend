package org.csu.tvds.models.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CarriageTreeNodeVO {
    public String label;
    public List<CarriageTreeNodeVO> children = new ArrayList<>();
}
