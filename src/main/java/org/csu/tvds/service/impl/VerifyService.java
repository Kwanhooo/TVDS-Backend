package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.Constant;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.models.dto.VerificationDO;
import org.csu.tvds.models.vo.PartOverviewVO;
import org.csu.tvds.models.vo.VerifyViewVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class VerifyService {
    @Resource
    private JobAssignMapper jobAssignMapper;
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;

    @Resource
    private ListService listService;

    public VerifyViewVO getVerifyView(String missionId) {
        JobAssign job = jobAssignMapper.selectOne(new QueryWrapper<JobAssign>().eq("dbId", missionId));

        // 任务不存在或者任务未激活
        if (job == null || job.getStatus().equals(Constant.JobStatus.INACTIVE)) {
            throw new BusinessException(1, "任务不存在或者任务未激活", "任务不存在或者任务未激活");
        }

        CompositeAlignedImage carriage = compositeAlignedImageMapper.selectById(job.getTargetCarriage());
        VerifyViewVO vo = new VerifyViewVO();
        BeanUtils.copyProperties(carriage, vo);
        vo.setUrl(carriage.getCompositeUrl());
        vo.setCompositeUrl(null);
        // 获得零件计数
        vo.setPartCount(compositeAlignedImageService.getPartCount(vo.getDbId()));

        // 获得附属零件
        vo.setAffiliateParts(new ArrayList<>());
        List<PartInfo> parts = listService.listCarriageParts(carriage.getDbId());
        for (PartInfo record : parts) {
            PartOverviewVO partVO = new PartOverviewVO();
            BeanUtils.copyProperties(record, partVO);
            String compositeId = record.getCompositeId();
            partVO.setCompositeId(compositeId);
            partVO.setCameraNo(carriage.getCameraNumber());
            partVO.setCarriageId(carriage.getCarriageId());
            vo.getAffiliateParts().add(partVO);
        }
        // 获得人员信息
        vo.setPersonnelSeq(job.getPersonnelSeq());

        return vo;
    }

    public Object handleVerificationResult(String missionId, VerificationDO verificationDO) {
        return verificationDO;
    }
}
