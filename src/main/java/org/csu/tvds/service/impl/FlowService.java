package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.CompositeAlignedImageStatus;
import org.csu.tvds.common.PartStatus;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.VisionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static org.csu.tvds.common.CompositeAlignedImageStatus.*;

@Service
public class FlowService {
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;
    @Resource
    private VisionService visionService;
    @Resource
    private PartInfoMapper partInfoMapper;

    public String auto() {
        dealWithCarriages();
        dealWithParts();
        return "success";
    }

    private void dealWithCarriages() {
        // 1. 找出所有还没有裁切的车厢
        QueryWrapper<CompositeAlignedImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status", CompositeAlignedImageStatus.CROP_FINISHED);
        List<CompositeAlignedImage> notCropCarriages = compositeAlignedImageMapper.selectList(queryWrapper);
        // 2. 对每个车厢进行流处理，直至所有都裁切
        notCropCarriages.forEach(carriage -> {
            int status = carriage.getStatus();
            switch (status) {
                case COMPOSITE_FINISHED:
                    startsWithOCR(carriage);
                    break;
                case OCR_FINISHED:
                    startsWithAlign(carriage);
                    break;
                case ALIGN_FINISHED:
                    startsCrop(carriage);
                    break;
                default:
                    break;
            }
        });
    }

    private void startsWithOCR(CompositeAlignedImage carriage) {
        String dbId = String.valueOf(carriage.getDbId());
        visionService.ocr(dbId);
        visionService.align(dbId);
        visionService.crop(dbId);
    }

    private void startsWithAlign(CompositeAlignedImage carriage) {
        String dbId = String.valueOf(carriage.getDbId());
        visionService.align(dbId);
        visionService.crop(dbId);
    }

    private void startsCrop(CompositeAlignedImage carriage) {
        String dbId = String.valueOf(carriage.getDbId());
        visionService.crop(dbId);
    }

    private void dealWithParts() {
        // 找到所有还为检测的零部件
        QueryWrapper<PartInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", PartStatus.UNDETECTED);
        List<PartInfo> undetectedParts = partInfoMapper.selectList(queryWrapper);
        undetectedParts.forEach(part -> {
            String dbId = String.valueOf(part.getDbId());
            visionService.detect(dbId);
        });
    }
}
