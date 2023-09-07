package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.ResponseCode;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.models.vo.CarriageStatsVO;
import org.csu.tvds.models.vo.DetectStatsVO;
import org.csu.tvds.models.vo.MissionStatsVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.csu.tvds.cache.MissionCache.missions;

@Service
public class StatisticsService {
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;
    @Resource
    private PartInfoMapper partInfoMapper;
    @Resource
    private JobAssignMapper jobAssignMapper;

    public CarriageStatsVO getCarriageStats() {
        List<String> distinctInspectionSeq = compositeAlignedImageMapper.selectDistinctInspectionSeq();
        List<String> distinctDetectedInspectionSeq = compositeAlignedImageMapper.selectDistinctDetectedInspectionSeq();
        CarriageStatsVO carriageStatsVO = new CarriageStatsVO();
        carriageStatsVO.setTotal(distinctInspectionSeq.size());
        distinctInspectionSeq.removeAll(distinctDetectedInspectionSeq);
        carriageStatsVO.setUndetected(distinctInspectionSeq.size());
        carriageStatsVO.setDetected(distinctDetectedInspectionSeq.size());
        carriageStatsVO.setDetecting(missions.stream().filter(mission -> mission.getStatus() == 1).count());
        return carriageStatsVO;
    }

    public List<MissionStatsVO> getMissionStats() {
        List<MissionStatsVO> result = new ArrayList<>();
//        List<PartInfo> parts = partInfoMapper.selectList
//                (
//                        new QueryWrapper<PartInfo>()
//                                .ne("status", PartStatus.UNDETECTED)
//                                .orderByDesc("checkTime")
//                                .last("limit 5")
//                );
        synchronized (missions) {
            for (int i = missions.size() - 1; i >= 0; i--) {
                MissionStatsVO vo = new MissionStatsVO();
                vo.setDbId(missions.get(i).getDbId());
                vo.setStatus(missions.get(i).getStatus());
                vo.setModel(missions.get(i).getModel());
                vo.setInspection(missions.get(i).getInspection());
                vo.setCarriageNo(missions.get(i).getCarriageNo());
                vo.setDefectBrief(missions.get(i).getDefectBrief());
                vo.setTime(missions.get(i).getTime());
                vo.setType(missions.get(i).getType());
                result.add(vo);
            }
        }

//        parts.forEach(part -> {
//            MissionStatsVO vo = new MissionStatsVO();
//            vo.setCarriageNo(part.getCarriageNo());
//            vo.setInspection(part.getInspectionSeq());
//            vo.setStatus(part.getStatus() == PartStatus.DEFECT ? MissionStatus.DEFECT : MissionStatus.NORMAL);
//            vo.setType(part.getPartName() + " 检测");
//            result.add(vo);
//        });
        return result;
    }

    public DetectStatsVO getDetectStats() {
        DetectStatsVO detectStatsVO = new DetectStatsVO();
        detectStatsVO.setTotalDefects(partInfoMapper.selectDistinctDefectCount());
        return detectStatsVO;
    }

    public Map<String, Integer> getUserStats(String uid) {
        long uidLong;
        try {
            uidLong = Long.parseLong(uid);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ARGUMENT_ILLEGAL, "uid不合法");
        }

        // 即将到来的任务数量
        QueryWrapper<JobAssign> upcomingMissionCountQuery = new QueryWrapper<>();
        upcomingMissionCountQuery.eq("assignee", uidLong).eq("status", Constant.JobStatus.INACTIVE);

        // 当前用户未完成的任务数量
        QueryWrapper<JobAssign> unverifiedMissionCountQuery = new QueryWrapper<>();
        unverifiedMissionCountQuery.eq("assignee", uidLong).eq("status", Constant.JobStatus.UNFINISHED);

        // 当前用户总共已完成的任务数量
        QueryWrapper<JobAssign> verifiedMissionCountQuery = new QueryWrapper<>();
        verifiedMissionCountQuery.eq("assignee", uidLong).eq("status", Constant.JobStatus.FINISHED);

        // 当前用户今日已完成的任务数量
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow = today.plusDays(1);

        QueryWrapper<JobAssign> todayFinishedMissionCountQuery = new QueryWrapper<>();
        todayFinishedMissionCountQuery.eq("assignee", uidLong)
                .eq("status", Constant.JobStatus.FINISHED)
                .between("updateTime", today, tomorrow);

        // 整个系统已完成的任务数量
        QueryWrapper<JobAssign> totalFinishedMissionCountQuery = new QueryWrapper<>();
        totalFinishedMissionCountQuery.eq("status", Constant.JobStatus.FINISHED);

        // 整个系统今天已完成的任务数量
        QueryWrapper<JobAssign> totalTodayFinishedMissionCountQuery = new QueryWrapper<>();
        totalTodayFinishedMissionCountQuery.eq("status", Constant.JobStatus.FINISHED)
                .between("updateTime", today, tomorrow);

        // 全部查询出来
        // 即将到来的任务数量
        int upcomingMissionCount = jobAssignMapper.selectCount(upcomingMissionCountQuery);
        // 当前用户未完成的任务数量
        int unverifiedMissionCount = jobAssignMapper.selectCount(unverifiedMissionCountQuery);
        // 当前用户总共已完成的任务数量
        int verifiedMissionCount = jobAssignMapper.selectCount(verifiedMissionCountQuery);
        // 当前用户今日已完成的任务数量
        int todayFinishedMissionCount = jobAssignMapper.selectCount(todayFinishedMissionCountQuery);
        // 整个系统已完成的任务数量
        int totalFinishedMissionCount = jobAssignMapper.selectCount(totalFinishedMissionCountQuery);
        // 整个系统今天已完成的任务数量
        int totalTodayFinishedMissionCount = jobAssignMapper.selectCount(totalTodayFinishedMissionCountQuery);

        // 组装结果
        Map<String, Integer> stats = new HashMap<>();
        stats.put("upcomingMissionCount", upcomingMissionCount);
        stats.put("unverifiedMissionCount", unverifiedMissionCount);
        stats.put("verifiedMissionCount", verifiedMissionCount);
        stats.put("todayFinishedMissionCount", todayFinishedMissionCount);
        stats.put("totalFinishedMissionCount", totalFinishedMissionCount);
        stats.put("totalTodayFinishedMissionCount", totalTodayFinishedMissionCount);

        return stats;
    }
}
