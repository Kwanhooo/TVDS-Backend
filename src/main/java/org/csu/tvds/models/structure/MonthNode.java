package org.csu.tvds.models.structure;

import java.util.ArrayList;

public class MonthNode extends Node {


    public MonthNode(String id, String month) {
        this.id = id;
        this.label = month;
        children = new ArrayList<>();
    }
}
