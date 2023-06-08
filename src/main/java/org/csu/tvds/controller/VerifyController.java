package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.models.dto.ConflictResolveViewRetrieveConditions;
import org.csu.tvds.models.dto.JobRetrieveCondition;
import org.csu.tvds.models.dto.VerificationDO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.service.JobAssignService;
import org.csu.tvds.service.impl.VerifyService;
import org.csu.tvds.shiro.auth.AuthUser;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 人工检测
 *
 * @author kwanho
 */
@RestController
@RequestMapping("/verify")
public class VerifyController {
    @Resource
    private JobAssignService jobAssignService;
    @Resource
    private VerifyService verifyService;

    /**
     * 获取用户的任务
     *
     * @param uid 用户id
     * @return 任务列表
     */
    @PostMapping("/getJobs/{currentPage}/{pageSize}")
    public CommonResponse<?> getJobs(
            @AuthUser String uid,
            @PathVariable String currentPage,
            @PathVariable String pageSize,
            @RequestBody(required = false) JobRetrieveCondition conditions
    ) {
        return CommonResponse.createForSuccess(jobAssignService.getJobsByUserId(
                uid,
                conditions,
                Long.parseLong(currentPage),
                Long.parseLong(pageSize)
        ));
    }

    /**
     * 获取任务视图，包含车厢信息及其附属零部件信息，人员编号
     *
     * @param missionId 任务id
     * @return 任务视图
     */
    @PostMapping("/missionDetail/{missionId}")
    public CommonResponse<?> getVerifyView(@PathVariable String missionId) {
        return CommonResponse.createForSuccess(verifyService.getVerifyView(missionId));
    }

    /**
     * 提交审核结果
     *
     * @param missionId    任务id
     * @param verification 审核结果体
     * @return 审核结果
     */
    @PostMapping("/submit/{missionId}")
    public CommonResponse<?> submitVerificationResult(
            @PathVariable String missionId,
            @RequestBody VerificationDO verification
    ) {
        return CommonResponse.createForSuccess(verifyService.handleVerificationResult(missionId, verification));
    }

    /**
     * 获取冲突解决视图
     * 仅限管理员使用
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @param conditions  检索条件
     * @return 冲突解决视图
     */
    @PostMapping("/getConflictResolveView/{currentPage}/{pageSize}")
    public CommonResponse<PaginationVO<?>> getConflictResolveView(
            @PathVariable String currentPage,
            @PathVariable String pageSize,
            @RequestBody(required = false) ConflictResolveViewRetrieveConditions conditions
    ) {
        return CommonResponse.createForSuccess(
                verifyService.getConflictResolveView(
                        conditions, Long.parseLong(currentPage), Long.parseLong(pageSize)
                )
        );
    }

    /**
     * 提交冲突解决结果
     *
     * @param partId 零部件id
     * @param result 解决结果
     * @return 解决结果
     */
    @PostMapping("/submitConflictResolveResult/{partId}/{result}")
    public CommonResponse<?> submitConflictResolveResult(
            @PathVariable String partId,
            @PathVariable String result
    ) {
        return CommonResponse.createForSuccess(verifyService.handleConflictResultSubmit(partId, result));
    }

}
