package org.csu.tvds.service;

import org.csu.tvds.models.vo.VisionResultVO;

public interface VisionService {
    VisionResultVO ocr(String dbId);

    VisionResultVO align(String dbId);

    VisionResultVO crop(String dbId);

    VisionResultVO defect(String dbId);
}
