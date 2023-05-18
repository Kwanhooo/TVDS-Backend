package org.csu.tvds.controller;

import org.csu.tvds.annotation.AdminPermission;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.service.impl.FlowService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flow")
public class FlowController {
    @Resource
    private FlowService flowService;

    /**
     * 自动流处理
     *
     * @return CommonResponse<?> 通用响应
     */
    @AdminPermission
    @RequestMapping("/auto")
    public CommonResponse<?> auto() {
        return CommonResponse.createForSuccess(flowService.auto());
    }
}
