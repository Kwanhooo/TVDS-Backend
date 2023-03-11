package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.MissionStatus;
import org.csu.tvds.common.PartStatus;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.vo.CarriageStatsVO;
import org.csu.tvds.models.vo.DetectStatsVO;
import org.csu.tvds.models.vo.MissionStatsVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.csu.tvds.cache.MissionCache.missions;

@Service
public class StatisticsService {
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;
    @Resource
    private PartInfoMapper partInfoMapper;

    public CarriageStatsVO getCarriageStats() {
        List<Integer> distinctInspectionSeq = compositeAlignedImageMapper.selectDistinctInspectionSeq();
        List<Integer> distinctDetectedInspectionSeq = compositeAlignedImageMapper.selectDistinctDetectedInspectionSeq();
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
        List<PartInfo> parts = partInfoMapper.selectList
                (
                        new QueryWrapper<PartInfo>()
                                .ne("status", PartStatus.UNDETECTED)
                                .orderByDesc("checkTime")
                                .last("limit 5")
                );
        synchronized (missions) {
            for (int i = missions.size() - 1; i >= 0; i--) {
                MissionStatsVO vo = new MissionStatsVO();
                vo.setCarriageNo(missions.get(i).getCarriageNo());
                vo.setInspection(missions.get(i).getInspection());
                vo.setStatus(missions.get(i).getStatus());
                vo.setType(missions.get(i).getType());
                result.add(vo);
            }
        }

        parts.forEach(part -> {
            MissionStatsVO vo = new MissionStatsVO();
            vo.setCarriageNo(part.getCarriageNo());
            vo.setInspection(part.getInspectionSeq());
            vo.setStatus(part.getStatus() == PartStatus.DEFECT ? MissionStatus.DEFECT : MissionStatus.NORMAL);
            vo.setType(part.getPartName() + " 检测");
            result.add(vo);
        });
        return result;
    }

    public DetectStatsVO getDetectStats() {
        DetectStatsVO detectStatsVO = new DetectStatsVO();
        detectStatsVO.setTotalDefects(partInfoMapper.selectDistinctDefectCount());
        return detectStatsVO;
    }
}
