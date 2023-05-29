package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.models.dto.VerificationDO;
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
    @PostMapping("/getJobs")
    public CommonResponse<?> getJobs(@AuthUser String uid) {
        return CommonResponse.createForSuccess(jobAssignService.getJobsByUserId(uid));
    }

    /**
     * 获取任务视图，包含车厢信息及其附属零部件信息，人员编号
     *
     * @param missionId 任务id
     * @return 任务视图
     */
    @PostMapping("/view/{missionId}")
    public CommonResponse<?> getVerifyView(@PathVariable String missionId) {
        return CommonResponse.createForSuccess(verifyService.getVerifyView(missionId));
    }


    /**
     * 提交审核结果
     *
     * @param missionId      任务id
     * @param verificationDO 审核结果体
     * @return 审核结果
     */
    @PostMapping("/submit/{missionId}")
    public CommonResponse<?> submitVerificationResult(@PathVariable String missionId, @RequestBody VerificationDO verificationDO) {
        return CommonResponse.createForSuccess(verifyService.handleVerificationResult(missionId, verificationDO));
    }

}
