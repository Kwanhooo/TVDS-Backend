package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.tvds.cache.RepaintTimerCache;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.PartVerifyStatus;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.DefectInfo;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.models.dto.ConflictResolveViewRetrieveConditions;
import org.csu.tvds.models.dto.SinglePartSubmitDO;
import org.csu.tvds.models.dto.VerificationDO;
import org.csu.tvds.models.dto.VerificationPartDO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.models.vo.PartOverviewVO;
import org.csu.tvds.models.vo.VerifyViewVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.DefectInfoMapper;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.csu.tvds.service.VisionService;
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
    private VisionService visionService;
    @Resource
    private DefectInfoMapper defectInfoMapper;
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
                part.setCommentA(p.getComment());
            } else if (personnelSeq.equals("B")) {
                if (p.getStatus() != PartVerifyStatus.UNVERIFIED)
                    part.setVerifyStatusB(p.getStatus());
                part.setCommentB(p.getComment());
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

            // 如果status，A，B三个字段不相同，则标记hasConflict为true，0通配符不参与比较
            if (part.getVerifyStatusA() != 0 && part.getVerifyStatusB() != 0) {
                if (!part.getVerifyStatusA().equals(part.getVerifyStatusB()) || !part.getVerifyStatusA().equals(part.getStatus())) {
                    part.setHasConflict(true);
                }
            }

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

    /**
     * 获取冲突解决视图
     *
     * @param conditions  查询条件
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @return 冲突解决视图
     */
    public PaginationVO<List<PartInfo>> getConflictResolveView(
            ConflictResolveViewRetrieveConditions conditions, long currentPage, long pageSize
    ) {
        QueryWrapper<PartInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hasConflict", true);

        PaginationVO<List<PartInfo>> result = new PaginationVO<>();
        Page<PartInfo> page = new Page<>(currentPage, pageSize);
        IPage<PartInfo> iPage = partInfoMapper.selectPage(page, queryWrapper);
        List<PartInfo> records = iPage.getRecords();

        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());
        result.setPage(records);

        return result;
    }

    public PartInfo handleConflictResultSubmit(String partId, String result) {
        long partDbId;
        int resultInt;
        try {
            partDbId = Long.parseLong(partId);
            resultInt = Integer.parseInt(result);
        } catch (Exception e) {
            throw new BusinessException(1, "参数错误", "参数错误");
        }
        PartInfo part = partInfoMapper.selectById(partDbId);
        if (part == null) {
            throw new BusinessException(1, "零件不存在", "零件不存在");
        }

        // 合并最终结果
        Integer originalStatus = part.getStatus();
        part.setStatus(resultInt);
        part.setHasConflict(false);

        // 重新绘制排队
        System.out.println("** 管理员审核后的重绘排队 **");
        RepaintTimerCache.produce(Long.parseLong(part.getCompositeId()));

        // 如果最终结果和原始结果不同，且原始是DEFECT，则从DefectInfo表中删除这条记录
        if (!originalStatus.equals(resultInt) && originalStatus.equals(PartVerifyStatus.DEFECT)) {
            QueryWrapper<DefectInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dbId", part.getDbId());
            defectInfoMapper.delete(queryWrapper);
        }

        // 更新零件信息至数据库
        partInfoMapper.updateById(part);

        // 如果最初不是DEFECT，现在是DEFECT，则添加一条记录至DefectInfo表
        // TODO： 可能有逻辑问题
        if (!originalStatus.equals(PartVerifyStatus.DEFECT) && resultInt == PartVerifyStatus.DEFECT) {
            if (defectInfoMapper.selectById(part.getDbId()) == null) {
                DefectInfo defectInfo = new DefectInfo();
                BeanUtils.copyProperties(part, defectInfo);
                defectInfo.setDbId(part.getDbId());
                defectInfoMapper.insert(defectInfo);
            }
        }

        return part;
    }

    /**
     * 处理单个零件提交的结果
     *
     * @param uid    用户id
     * @param result 单个零件提交的结果
     * @return 零件信息
     */
    public PartInfo handleSinglePartSubmit(String uid, SinglePartSubmitDO result) {
        Long partDbId = result.getPartId();

        System.out.println("partDbId => " + partDbId);

        // 1. 校验零件是否在库中

        PartInfo part = partInfoMapper.selectOne(new QueryWrapper<PartInfo>().eq("dbId", partDbId));
        if (part == null) {
            throw new BusinessException(1, "零件不存在", "零件不存在");
        }

        // 2. 查询这个用户的任务人员编号
        // 2.1 查到这个零件的父车厢
        String compositeId = part.getCompositeId();
        CompositeAlignedImage parentCarriage = compositeAlignedImageMapper.selectOne(new QueryWrapper<CompositeAlignedImage>().eq("dbId", compositeId));
        if (parentCarriage == null) {
            throw new BusinessException(1, "父车厢不存在", "父车厢不存在");
        }

        // 2.2 查到这个用户的任务人员编号
        QueryWrapper<JobAssign> jobAssignQueryWrapper = new QueryWrapper<JobAssign>()
                .eq("targetCarriage", compositeId)
                .eq("assignee", Long.parseLong(uid));
        JobAssign job = jobAssignMapper.selectOne(jobAssignQueryWrapper);
        System.out.println(compositeId);
        System.out.println(Long.parseLong(uid));
        System.out.println(job);

        String personnelSeq = job.getPersonnelSeq();

        // 3. 更新零件信息
        // 3.1 更新零件的状态
        if (personnelSeq.equals("A")) {
            part.setVerifyStatusA(result.getStatus());
            part.setCommentA(result.getComment());
        } else if (personnelSeq.equals("B")) {
            part.setVerifyStatusB(result.getStatus());
            part.setCommentB(result.getComment());
        } else {
            throw new BusinessException(1, "任务人员编号错误", "任务人员编号错误");
        }

        // 3.2 更新零件的hasConflict字段
        if (part.getVerifyStatusA() != 0 && part.getVerifyStatusB() != 0) {
            if (!part.getVerifyStatusA().equals(part.getVerifyStatusB()) || !part.getVerifyStatusA().equals(part.getStatus())) {
                part.setHasConflict(true);
            }
        }

        // 3.4 保存零件信息至数据库
        partInfoMapper.updateById(part);

        // 4 检查任务在提交完这个零件后是否已经完成，条件是所有当前有异常的零件都已经审核完毕
        // 4.1 查询当前任务下所有异常的零件
        QueryWrapper<PartInfo> partInfoQueryWrapper = new QueryWrapper<PartInfo>()
                .eq("compositeId", compositeId)
                .eq("status", PartVerifyStatus.DEFECT);
        List<PartInfo> defectParts = partInfoMapper.selectList(partInfoQueryWrapper);

        // 4.2 检查此人的状态位是否有未提交
        for (PartInfo p : defectParts) {
            if (personnelSeq.equals("A")) {
                if (p.getVerifyStatusA() == PartVerifyStatus.UNVERIFIED) {
                    return part;
                }
            } else {
                if (p.getVerifyStatusB() == PartVerifyStatus.UNVERIFIED) {
                    return part;
                }
            }
        }

        // 4.3 如果没有未提交的状态位，则任务完成
        // 4.3.1 更新任务状态
        job.setStatus(Constant.JobStatus.FINISHED);

        // 4.3.2 更新任务信息至数据库
        jobAssignMapper.updateById(job);

        return part;
    }
}
