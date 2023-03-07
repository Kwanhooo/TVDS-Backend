package org.csu.tvds.util;

import org.csu.tvds.models.structure.DayNode;
import org.csu.tvds.models.structure.MonthNode;
import org.csu.tvds.models.structure.Node;
import org.csu.tvds.models.structure.YearNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TreeUtil {
    @Resource
    private NumberParser numberParser;

    public List<YearNode> buildDateTree(List<LocalDate> dates) {
        List<YearNode> tree = new ArrayList<>();
        dates.forEach(date -> {
            String year = String.valueOf(date.getYear());
            System.out.println(date);
            boolean hasYear = false;
            for (YearNode yearNode : tree) {
                if (Objects.equals(yearNode.label, year)) {
                    hasYear = true;
                    boolean hasMonth = false;
                    for (Node monthNode : yearNode.children) {
                        if (Objects.equals(monthNode.label, String.valueOf(date.getMonthValue()))) {
                            hasMonth = true;
                            monthNode.children.add(new DayNode(monthNode.id + numberParser.parseTwoDigits(date.getDayOfMonth()), String.valueOf(date.getMonthValue())));
                            break;
                        }
                    }
                    if (!hasMonth) {
                        MonthNode monthNode = new MonthNode(yearNode.id + numberParser.parseTwoDigits(date.getMonthValue()), String.valueOf(date.getMonthValue()));
                        monthNode.children.add(new DayNode(monthNode.id + numberParser.parseTwoDigits(date.getDayOfMonth()), String.valueOf(date.getMonthValue())));
                        yearNode.children.add(monthNode);
                    }
                    break;
                }
            }
            if (!hasYear) {
                YearNode yearNode = new YearNode(year, year);
                MonthNode monthNode = new MonthNode(yearNode.id + numberParser.parseTwoDigits(date.getMonthValue()), String.valueOf(date.getMonthValue()));
                monthNode.children.add(new DayNode(monthNode.id + numberParser.parseTwoDigits(date.getDayOfMonth()), String.valueOf(date.getMonthValue())));
                yearNode.children.add(monthNode);
                tree.add(yearNode);
            }
        });
        return tree;
    }
}
