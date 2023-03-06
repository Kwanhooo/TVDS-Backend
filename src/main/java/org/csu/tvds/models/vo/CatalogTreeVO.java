package org.csu.tvds.models.vo;

import lombok.Data;
import org.csu.tvds.models.structure.Node;

import java.util.List;

@Data
public class CatalogTreeVO {
    private List<Node> tree;
}
