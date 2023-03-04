package org.csu.tvds.service.impl;

import org.csu.tvds.common.CompositeAlignedImageStatus;
import org.csu.tvds.core.OCRModel;
import org.csu.tvds.core.io.Output;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.OcrResultVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.service.VisionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.csu.tvds.common.PathConfig.BLOB_BASE;

@Service
public class VisionServiceImpl implements VisionService {
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Override
    public OcrResultVO ocr(String dbId) {
        OcrResultVO ocrResultVO = new OcrResultVO();
        CompositeAlignedImage carriage = compositeAlignedImageMapper.selectById(dbId);
        if (carriage == null) {
            ocrResultVO.setSucceed(false);
            ocrResultVO.setMessage("车厢不存在");
            return ocrResultVO;
        }
        if (!carriage.getStatus().equals(CompositeAlignedImageStatus.COMPOSITE_FINISHED)) {
            ocrResultVO.setSucceed(false);
            ocrResultVO.setMessage("该车厢不可进行OCR操作，当前状态码为：" + carriage.getStatus());
            return ocrResultVO;
        }
        OCRModel ocrModel = new OCRModel();
        // THIS STEP MAY TAKE A LONG TIME
        Output<String> output = ocrModel.dispatch(BLOB_BASE + carriage.getCompositeUrl());
        if (output.isSucceed()) {
            ocrResultVO.setSucceed(true);
            ocrResultVO.setMessage("OCR识别成功");
            String ocrText = output.getData();
            String[] split = ocrText.split("_");
            String model = split[4];
            String carriageId = split[5];
            carriage.setModel(model);
            carriage.setCarriageId(Integer.valueOf(carriageId));
            compositeAlignedImageMapper.updateById(carriage);
            CarriageOverviewVO vo = new CarriageOverviewVO();
            BeanUtils.copyProperties(carriage, vo);
            vo.setUrl(carriage.getCompositeUrl());
            vo.setCompositeUrl(null);
            ocrResultVO.setData(vo);
        } else {
            ocrResultVO.setSucceed(false);
            ocrResultVO.setMessage("OCR识别失败，无法识别出型号");
        }
        return ocrResultVO;
    }
}
