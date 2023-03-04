package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageTreeNodeVO;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tree")
public class TreeController {
    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;

    @PostMapping("/carriage")
    public CommonResponse<?> getCarriageTree(@RequestBody(required = false) CarriageRetrieveConditions conditions) {
        CarriageTreeNodeVO root = compositeAlignedImageService.buildTree(conditions);
        return CommonResponse.createForSuccess(root);
    }

}
