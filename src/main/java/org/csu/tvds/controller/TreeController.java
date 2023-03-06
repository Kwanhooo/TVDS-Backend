package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.csu.tvds.service.OriginImageService;
import org.csu.tvds.service.PartInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tree")
public class TreeController {
    @Resource
    private OriginImageService originImageService;

    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;

    @Resource
    private PartInfoService partInfoService;

    @PostMapping("/origin")
    public CommonResponse<?> getOriginTree() {
        return CommonResponse.createForSuccess(originImageService.buildDateTree());
    }

    @PostMapping("/carriage")
    public CommonResponse<?> getCarriageTree() {
        return CommonResponse.createForSuccess(compositeAlignedImageService.buildDateTree());
    }

    @PostMapping("/part")
    public CommonResponse<?> getPartTree() {
        return CommonResponse.createForSuccess(partInfoService.buildCatalogTree());
    }

}
