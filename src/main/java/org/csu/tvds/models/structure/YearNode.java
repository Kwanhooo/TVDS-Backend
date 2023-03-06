package org.csu.tvds.models.structure;

import java.util.ArrayList;

public class YearNode extends Node {
    public YearNode(String id, String year) {
        this.id = id;
        this.label = year;
        children = new ArrayList<>();
    }
}
