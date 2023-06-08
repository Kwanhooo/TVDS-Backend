package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.PartVerifyStatus;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.models.dto.VerificationDO;
import org.csu.tvds.models.dto.VerificationPartDO;
import org.csu.tvds.models.vo.PartOverviewVO;
import org.csu.tvds.models.vo.VerifyViewVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VerifyService {
    @Resource
    private JobAssignMapper jobAssignMapper;
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;
    @Resource
    private PartInfoMapper partInfoMapper;
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

    /**
     * 审核任务递交
     *
     * @param missionId    任务id
     * @param verification 审核结果
     * @return 审核结果
     */
    public Object handleVerificationResult(String missionId, VerificationDO verification) {
        System.out.println("收到审核结果：" + verification);

        List<VerificationPartDO> results = verification.getResults();

        JobAssign job = jobAssignMapper.selectOne(new QueryWrapper<JobAssign>().eq("dbId", missionId));

        // 任务不存在或者任务未激活
        if (job == null || job.getStatus().equals(Constant.JobStatus.INACTIVE)) {
            throw new BusinessException(1, "任务不存在或者任务未激活", "任务不存在或者任务未激活");
        }

        String personnelSeq = job.getPersonnelSeq();

        // 最终要返回的结果数组
        ArrayList<PartInfo> verifiedParts = new ArrayList<>();

        // 依次修改零件的状态
        results.forEach(p -> {
            PartInfo part = partInfoMapper.selectOne(new QueryWrapper<PartInfo>().eq("dbId", p.getPartId()));
            if (part == null) {
                throw new BusinessException(1, "零件不存在", "零件不存在");
            }
            if (personnelSeq.equals("A")) {
                if (p.getStatus() != PartVerifyStatus.UNVERIFIED)
                    part.setVerifyStatusA(p.getStatus());
            } else if (personnelSeq.equals("B")) {
                if (p.getStatus() != PartVerifyStatus.UNVERIFIED)
                    part.setVerifyStatusB(p.getStatus());
            } else {
                throw new BusinessException(1, "人员信息错误", "人员信息错误");
            }

            // 尝试合并AB两个人的结果至status字段
            // if (part.getVerifyStatusA() != PartVerifyStatus.UNVERIFIED && part.getVerifyStatusB() != PartVerifyStatus.UNVERIFIED) {
            //     System.out.println("A、B两个人都已经完成审核，尝试合并结果...");
            //     // 只要有一个人认为是DEFECT，就是DEFECT；两个人都认为是NORMAL，才是NORMAL
            //     if (part.getVerifyStatusA() == PartVerifyStatus.DEFECT || part.getVerifyStatusB() == PartVerifyStatus.DEFECT) {
            //         part.setStatus(PartVerifyStatus.DEFECT);
            //     } else {
            //         part.setStatus(PartVerifyStatus.NORMAL);
            //     }
            // }
            
            // 更新零件信息至数据库
            partInfoMapper.updateById(part);

            // 添加至返回结果
            verifiedParts.add(part);
        });

        // 修改任务状态为FINISHED
        job.setStatus(Constant.JobStatus.FINISHED);
        job.setUpdateTime(LocalDateTime.now());
        jobAssignMapper.updateById(job);

        return verifiedParts;
    }

}
