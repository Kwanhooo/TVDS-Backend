package org.csu.tvds.models.structure;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    public String id;
    public String label;
    public List<Node> children = new ArrayList<>();

    public Node() {
    }

    public Node(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public Node(String id, String label, List<Node> children) {
        this.id = id;
        this.label = label;
        this.children = children;
    }
}
