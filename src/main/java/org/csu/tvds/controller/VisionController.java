package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.service.VisionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/vision")
public class VisionController {
    @Resource
    private VisionService visionService;

    @PostMapping("/ocr/{dbId}")
    public CommonResponse<?> ocr(@PathVariable String dbId) {
        return CommonResponse.createForSuccess(visionService.ocr(dbId));
    }

    @PostMapping("/align/{dbId}")
    public CommonResponse<?> align(@PathVariable String dbId) {
        return CommonResponse.createForSuccess(visionService.align(dbId));
    }

    @PostMapping("/crop/{dbId}")
    public CommonResponse<?> crop(@PathVariable String dbId) {
        return CommonResponse.createForSuccess(visionService.crop(dbId));
    }

    @PostMapping("/defect/{dbId}")
    public CommonResponse<?> defect(@PathVariable String dbId) {
        return CommonResponse.createForSuccess(visionService.defect(dbId));
    }

}
