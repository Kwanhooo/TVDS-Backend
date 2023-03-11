package org.csu.tvds.cache;

import org.csu.tvds.models.vo.MissionStatsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务缓存
 *
 * @author Kwanho
 */
public class MissionCache {
    static {
        missions = new ArrayList<>();
    }

    public static final List<MissionStatsVO> missions;
}
