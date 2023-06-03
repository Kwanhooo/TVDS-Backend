package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.models.dto.JobRetrieveCondition;
import org.csu.tvds.models.vo.JobAssignVO;
import org.csu.tvds.models.vo.PaginationVO;

import java.util.List;

/**
 * @author kwanho
 * @description 针对表【job_assign】的数据库操作Service
 * @createDate 2023-05-18 11:16:12
 */
public interface JobAssignService extends IService<JobAssign> {
    void autoAssign(Long compositeId);

    void tryToActivate(Long compositeId);

    PaginationVO<List<JobAssignVO>> getJobsByUserId(String uid, JobRetrieveCondition conditions, long l, long parseLong);
}
