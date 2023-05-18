package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.tvds.entity.mysql.JobAssign;
import org.csu.tvds.service.JobAssignService;
import org.csu.tvds.persistence.mysql.JobAssignMapper;
import org.springframework.stereotype.Service;

/**
* @author kwanho
* @description 针对表【job_assign】的数据库操作Service实现
* @createDate 2023-05-18 11:16:12
*/
@Service
public class JobAssignServiceImpl extends ServiceImpl<JobAssignMapper, JobAssign>
    implements JobAssignService{

}




