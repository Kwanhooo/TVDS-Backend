package org.csu.tvds.controller;

import org.csu.tvds.service.CompositeAlignedImageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/detail")
public class DetailController {
    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;

}
