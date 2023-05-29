package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.JobAssign;

import java.util.List;

/**
 * @author kwanho
 * @description 针对表【job_assign】的数据库操作Service
 * @createDate 2023-05-18 11:16:12
 */
public interface JobAssignService extends IService<JobAssign> {
    void autoAssign(Long compositeId);

    void tryToActivate(Long compositeId);

    List<JobAssign> getJobsByUserId(String uid);
}
