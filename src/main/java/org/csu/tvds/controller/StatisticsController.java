package org.csu.tvds.controller;

import org.csu.tvds.annotation.AdminPermission;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.service.impl.StatisticsService;
import org.csu.tvds.shiro.auth.AuthUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Resource
    private StatisticsService statisticsService;

    @AdminPermission
    @RequestMapping("/carriage")
    public CommonResponse<?> carriageStats() {
        return CommonResponse.createForSuccess(statisticsService.getCarriageStats());
    }

    @AdminPermission
    @RequestMapping("/mission")
    public CommonResponse<?> missionStats() {
        return CommonResponse.createForSuccess(statisticsService.getMissionStats());
    }

    @AdminPermission
    @RequestMapping("/detect")
    public CommonResponse<?> detectStats() {
        return CommonResponse.createForSuccess(statisticsService.getDetectStats());
    }

    @RequestMapping("/user")
    public CommonResponse<?> userStats(@AuthUser String uid) {
        return CommonResponse.createForSuccess(statisticsService.getUserStats(uid));
    }
}
