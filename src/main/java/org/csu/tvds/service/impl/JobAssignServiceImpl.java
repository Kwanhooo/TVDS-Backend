package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.PartStatus;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.models.dto.JobRetrieveCondition;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.JobAssignVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.csu.tvds.persistence.mysql.UserMapper;
import org.csu.tvds.service.JobAssignService;
import org.csu.tvds.util.SequenceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kwanho
 */
@Service
public class JobAssignServiceImpl extends ServiceImpl<JobAssignMapper, JobAssign>
        implements JobAssignService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ListService listService;

    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Resource
    private JobAssignMapper jobAssignMapper;

    @Override
    public void autoAssign(Long compositeId) {
        System.out.println("分配任务，compositeId = " + compositeId);

        // 1. 获得每个人未完成的任务数
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.ne("role", Constant.Role.ADMIN);
        Map<Long, Integer> map = new HashMap<>(); // uid -> count
        userMapper.selectList(userQueryWrapper).forEach(user -> map.put(Long.valueOf(user.getUserId()), 0));

        QueryWrapper<JobAssign> jobAssignQueryWrapper = new QueryWrapper<>();
        jobAssignQueryWrapper.orderByAsc("assignee");
        jobAssignQueryWrapper.ne("status", Constant.JobStatus.FINISHED);

        this.list(jobAssignQueryWrapper).forEach(job -> {
            Long uid = job.getAssignee();
            if (map.containsKey(uid)) {
                map.put(uid, map.get(uid) + 1);
            } else {
                map.put(uid, 1);
            }
        });

//        Long minUid = null;
//        Integer minCount = Integer.MAX_VALUE;
//        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
//            if (entry.getValue() < minCount) {
//                minUid = entry.getKey();
//                minCount = entry.getValue();
//            }
//        }

        // 2. 找到未完成任务数最少的两个人
        Long personnelA = null;
        Long minCountA = Long.MAX_VALUE;
        Long personnelB = null;
        Long minCountB = Long.MAX_VALUE;
        for (Long uid : map.keySet()) {
            if (map.get(uid) < minCountA) {
                personnelB = personnelA;
                minCountB = minCountA;
                personnelA = uid;
                minCountA = Long.valueOf(map.get(uid));
            } else if (map.get(uid) < minCountB) {
                personnelB = uid;
                minCountB = Long.valueOf(map.get(uid));
            }
        }

        System.out.println("personnelA = " + personnelA);
        System.out.println("personnelB = " + personnelB);

        // 3. 分配任务
        JobAssign jobA = new JobAssign();
        jobA.setDbId(SequenceUtil.gen());
        jobA.setPersonnelSeq("A");
        jobA.setAssignee(personnelA);
        jobA.setDeadline(null);
        jobA.setTargetCarriage(compositeId);
        jobA.setStatus(Constant.JobStatus.INACTIVE);
        jobA.setCreateTime(LocalDateTime.now());
        jobA.setUpdateTime(LocalDateTime.now());

        this.save(jobA);

        JobAssign jobB = new JobAssign();
        jobB.setDbId(SequenceUtil.gen());
        jobB.setPersonnelSeq("B");
        jobB.setAssignee(personnelB);
        jobB.setDeadline(null);
        jobB.setTargetCarriage(compositeId);
        jobB.setStatus(Constant.JobStatus.INACTIVE);
        jobB.setCreateTime(LocalDateTime.now());
        jobB.setUpdateTime(LocalDateTime.now());

        this.save(jobB);
    }

    @Override
    public void tryToActivate(Long compositeId) {
        // 获取compositeId对应车厢的零件
        List<PartInfo> infos = listService.listCarriageParts(compositeId);
        // 获取compositeId对应车厢的已完成零件数，遍历infos
        int count = 0;
        for (PartInfo info : infos) {
            if (info.getStatus().equals(PartStatus.UNDETECTED)) {
                count++;
            }
        }
        // count == 0，激活任务
        if (count == 0) {
            QueryWrapper<JobAssign> jobAssignQueryWrapper = new QueryWrapper<>();
            jobAssignQueryWrapper.eq("targetCarriage", compositeId);
            List<JobAssign> jobs = this.list(jobAssignQueryWrapper);
            jobs.forEach(job -> {
                job.setStatus(Constant.JobStatus.UNFINISHED);
                System.out.println("激活任务，job = " + job);
                jobAssignMapper.updateById(job);
            });
        } else {
            System.out.println("暂不激活任务，该车厢未完成检测个数 => " + count);
        }
    }

    @Override
    public PaginationVO<List<JobAssignVO>> getJobsByUserId(String uid, JobRetrieveCondition conditions, long currentPage, long pageSize) {
        QueryWrapper<JobAssign> wrapper = new QueryWrapper<>();
        wrapper.eq("assignee", uid);
        wrapper.ne("status", Constant.JobStatus.INACTIVE);

        Page<JobAssign> page = new Page<>(currentPage, pageSize);
        IPage<JobAssign> iPage = this.page(page, wrapper);
        List<JobAssign> records = iPage.getRecords();

        PaginationVO<List<JobAssignVO>> result = new PaginationVO<>();
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());

        List<JobAssignVO> vos = new ArrayList<>();
        records.forEach(r -> {
            JobAssignVO vo = new JobAssignVO();

            CompositeAlignedImage carriage = compositeAlignedImageMapper.selectOne(
                    new QueryWrapper<CompositeAlignedImage>()
                            .eq("dbId", r.getTargetCarriage())
            );
            CarriageOverviewVO carriageOverviewVO = new CarriageOverviewVO();
            BeanUtils.copyProperties(carriage, carriageOverviewVO);
            carriageOverviewVO.setUrl(carriage.getCompositeUrl());
            carriageOverviewVO.setCompositeUrl(null);

            BeanUtils.copyProperties(carriageOverviewVO, vo);
            BeanUtils.copyProperties(r, vo);

            vo.setDbId(carriageOverviewVO.getDbId());
            vo.setMissionId(String.valueOf(r.getDbId()));

            vos.add(vo);
        });

        result.setPage(vos);

        return result;
    }
}




