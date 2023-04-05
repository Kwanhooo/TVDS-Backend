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

    @PostMapping("/part")
    public CommonResponse<?> listCarriageParts(Long id) {
        return CommonResponse.createForSuccess(listService.listCarriageParts(id));
    }
}
