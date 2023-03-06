package org.csu.tvds.models.vo;

import lombok.Data;
import org.csu.tvds.models.structure.YearNode;

import java.util.List;

@Data
public class DateTreeVO {
    private List<YearNode> tree;
}
