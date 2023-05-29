package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.PartStatus;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.csu.tvds.persistence.mysql.UserMapper;
import org.csu.tvds.service.JobAssignService;
import org.csu.tvds.util.SequenceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
            JobAssign job = this.getOne(jobAssignQueryWrapper);
            job.setStatus(Constant.JobStatus.UNFINISHED);
            System.out.println("激活任务，job = " + job);
            this.updateById(job);
        } else {
            System.out.println("不激活任务，count = " + count);
        }
    }

    @Override
    public List<JobAssign> getJobsByUserId(String uid) {
        QueryWrapper<JobAssign> jobAssignQueryWrapper = new QueryWrapper<>();
        jobAssignQueryWrapper.eq("assignee", uid);
        jobAssignQueryWrapper.ne("status", Constant.JobStatus.INACTIVE);
        return this.list(jobAssignQueryWrapper);
    }
}




