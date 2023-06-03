package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.service.impl.ListService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/list")
public class ListController {
    @Resource
    private ListService listService;

    /**
     * 获取指定车厢的附属零部件
     *
     * @param id 车厢id
     * @return 附属零部件列表
     */
    @PostMapping("/part")
    public CommonResponse<?> listCarriageParts(Long id) {
        return CommonResponse.createForSuccess(listService.listCarriageParts(id));
    }
}
