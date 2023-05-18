package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.common.ResponseCode;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.service.impl.ListService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ListService listService;

    @PostMapping("/part/{id}")
    public CommonResponse<?> getAffiliateParts(@PathVariable String id) {
        if (!id.matches("\\d+")) {
            throw new BusinessException(ResponseCode.ARGUMENT_ILLEGAL, "id不是数字");
        }
        return CommonResponse.createForSuccess(listService.listCarriageParts(Long.parseLong(id)));
    }
}
