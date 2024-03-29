package org.csu.tvds.controller;

import lombok.extern.slf4j.Slf4j;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.service.impl.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * FOR TEST ONLY
 * PRIVATE INTERFACE
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Resource
    private TestService testService;

    @PostMapping("/uploadCarriage")
    public CommonResponse<?> uploadCarriage(@RequestParam("file") MultipartFile file) {
        return CommonResponse.createForSuccess(testService.handleCarriageUpload(file));
    }

    @RequestMapping("/log")
    public CommonResponse<?> log() {
        log.debug("Hello, World!");
        return CommonResponse.createForSuccess("OK");
    }

    @RequestMapping("/rollback")
    public CommonResponse<?> clear() {
        testService.rollback();
        return CommonResponse.createForSuccess("OK");
    }
}
