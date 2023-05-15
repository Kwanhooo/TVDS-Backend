package org.csu.tvds.service.impl;

import org.apache.commons.io.FileUtils;
import org.csu.tvds.common.CompositeAlignedImageStatus;
import org.csu.tvds.common.MissionStatus;
import org.csu.tvds.common.PartStatus;
import org.csu.tvds.common.PathConfig;
import org.csu.tvds.core.AlignModel;
import org.csu.tvds.core.CropModel;
import org.csu.tvds.core.DefectModel;
import org.csu.tvds.core.OCRModel;
import org.csu.tvds.core.io.Output;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.MissionStatsVO;
import org.csu.tvds.models.vo.VisionResultVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.DefectInfoService;
import org.csu.tvds.service.VisionService;
import org.csu.tvds.util.SequenceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;

import static org.csu.tvds.cache.MissionCache.missions;
import static org.csu.tvds.common.PathConfig.BLOB_BASE;

@Service
public class VisionServiceImpl implements VisionService {
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Resource
    private PartInfoMapper partInfoMapper;

    @Resource
    private DefectInfoService defectInfoService;

    @Override
    public VisionResultVO ocr(String dbId) {
        VisionResultVO ocrResultVO = new VisionResultVO();
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
        MissionStatsVO mission = new MissionStatsVO(
                SequenceUtil.gen(),
                MissionStatus.PENDING,
                carriage.getInspectionSeq(),
                carriage.getCarriageNo(),
                "车型识别"
        );
        missions.add(mission);
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
            carriage.setStatus(CompositeAlignedImageStatus.OCR_FINISHED);
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
        missions.forEach(m -> {
            if (m.getUid() == mission.getUid()) {
                m.setStatus(MissionStatus.TERMINATED);
            }
        });
        return ocrResultVO;
    }

    @Override
    public VisionResultVO align(String dbId) {
        VisionResultVO alignResultVO = new VisionResultVO();
        CompositeAlignedImage carriage = compositeAlignedImageMapper.selectById(dbId);
        if (carriage == null) {
            alignResultVO.setSucceed(false);
            alignResultVO.setMessage("车厢不存在");
            return alignResultVO;
        }
        if (!carriage.getStatus().equals(CompositeAlignedImageStatus.OCR_FINISHED)) {
            alignResultVO.setSucceed(false);
            alignResultVO.setMessage("该车厢不可进行配准操作，当前状态码为：" + carriage.getStatus());
            return alignResultVO;
        }
        MissionStatsVO mission = new MissionStatsVO(SequenceUtil.gen(), MissionStatus.PENDING, carriage.getInspectionSeq(), carriage.getCarriageNo(), "车厢图形配准");
        missions.add(mission);
        AlignModel alignModel = new AlignModel();
        Output<String> output = alignModel.dispatch(BLOB_BASE + carriage.getCompositeUrl(), carriage.getModel());
        if (output.isSucceed()) {
            alignResultVO.setSucceed(true);
            alignResultVO.setMessage("配准成功");
            String alignFilename = carriage.getInspectionSeq() + "_" + carriage.getCameraNumber() + "_" + carriage.getCarriageNo() + ".jpg";
            carriage.setStatus(CompositeAlignedImageStatus.ALIGN_FINISHED);
            carriage.setAlignedUrl("marked/" + alignFilename);
            carriage.setAlignTime(LocalDateTime.now());
            compositeAlignedImageMapper.updateById(carriage);
            alignResultVO.setData(carriage);
        } else {
            alignResultVO.setSucceed(false);
            alignResultVO.setMessage("配准失败");
            alignResultVO.setData(null);
        }
        missions.forEach(m -> {
            if (m.getUid() == mission.getUid()) {
                m.setStatus(MissionStatus.TERMINATED);
            }
        });
        return alignResultVO;
    }

    @Override
    public VisionResultVO crop(String dbId) {
        VisionResultVO cropResultVO = new VisionResultVO();
        CompositeAlignedImage carriage = compositeAlignedImageMapper.selectById(dbId);
        if (carriage == null) {
            cropResultVO.setSucceed(false);
            cropResultVO.setMessage("车厢不存在");
            return cropResultVO;
        }
        if (!carriage.getStatus().equals(CompositeAlignedImageStatus.ALIGN_FINISHED)) {
            cropResultVO.setSucceed(false);
            cropResultVO.setMessage("该车厢不可进行裁切操作，当前状态码为：" + carriage.getStatus());
            return cropResultVO;
        }
        MissionStatsVO mission = new MissionStatsVO(SequenceUtil.gen(), MissionStatus.PENDING, carriage.getInspectionSeq(), carriage.getCarriageNo(), "零部件裁切");
        missions.add(mission);
        CropModel cropModel = new CropModel();
        String alignedUrl = carriage.getAlignedUrl();
        String imagePath = alignedUrl.replace("marked", "aligned");
        Output<Boolean> output = cropModel.dispatch(BLOB_BASE + imagePath, carriage.getModel());
        if (!output.isSucceed()) {
            cropResultVO.setSucceed(false);
            cropResultVO.setMessage("裁切失败");
            return cropResultVO;
        }
        String dir = carriage.getInspectionSeq() + "_" + carriage.getCameraNumber() + "_" + carriage.getCarriageNo();
        String outputDir = PathConfig.PARTS_BASE + dir;
        File outputDirFile = new File(outputDir);
        FileUtils.iterateFiles(outputDirFile, null, true).forEachRemaining(file -> {
            PartInfo part = new PartInfo();
            part.setDbId(SequenceUtil.gen());
            part.setId(dir + "_" + file.getName().split("\\.")[0]);
            part.setPartName(part.getId().split("_")[3]);
            part.setCarriageNo(carriage.getCarriageNo());
            part.setInspectionSeq(carriage.getInspectionSeq());
            part.setModel(carriage.getModel());
            part.setCompositeId(String.valueOf(carriage.getDbId()));
            part.setImageUrl("parts/" + dir + "/" + file.getName());
            part.setStatus(PartStatus.UNDETECTED);
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime createTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonth(), currentDateTime.getDayOfMonth(), 0, 0, 0);
            part.setCreateTime(createTime);
            part.setUpdateTime(currentDateTime);
            partInfoMapper.insert(part);
        });
        carriage.setStatus(CompositeAlignedImageStatus.CROP_FINISHED);
        compositeAlignedImageMapper.updateById(carriage);
        cropResultVO.setSucceed(true);
        cropResultVO.setMessage("裁切成功");
        CarriageOverviewVO vo = new CarriageOverviewVO();
        BeanUtils.copyProperties(carriage, vo);
        vo.setUrl(carriage.getCompositeUrl());
        vo.setCompositeUrl(null);
        cropResultVO.setData(vo);
        missions.forEach(m -> {
            if (m.getUid() == mission.getUid()) {
                m.setStatus(MissionStatus.TERMINATED);
            }
        });
        return cropResultVO;
    }

    @Override
    public VisionResultVO detect(String dbId) {
        VisionResultVO defectResultVO = new VisionResultVO();
        PartInfo partInfo = partInfoMapper.selectById(dbId);
        if (partInfo == null) {
            defectResultVO.setSucceed(false);
            defectResultVO.setMessage("零件不存在");
            return defectResultVO;
        }
        if (!partInfo.getStatus().equals(PartStatus.UNDETECTED)) {
            defectResultVO.setSucceed(false);
            defectResultVO.setMessage("该零件不可进行缺陷检测操作，当前状态码为：" + partInfo.getStatus());
            return defectResultVO;
        }
        MissionStatsVO mission = new MissionStatsVO(
                SequenceUtil.gen(),
                MissionStatus.PENDING,
                partInfo.getInspectionSeq(),
                partInfo.getCarriageNo(),
                partInfo.getPartName() + " 检测"
        );
        missions.add(mission);
        DefectModel defectModel = new DefectModel();
        Output<Boolean> output = defectModel.dispatch(BLOB_BASE + partInfo.getImageUrl());
        if (!output.isSucceed()) {
            defectResultVO.setSucceed(false);
            defectResultVO.setMessage("缺陷检测失败");
            return defectResultVO;
        }
        if (output.getData()) {
            partInfo.setStatus(PartStatus.DEFECT);
            // 引入到异常信息表中
            defectInfoService.newDetection(partInfo);
        } else {
            partInfo.setStatus(PartStatus.NORMAL);
        }

        partInfo.setCheckTime(LocalDateTime.now());
        partInfoMapper.updateById(partInfo);
        defectResultVO.setSucceed(true);
        defectResultVO.setMessage("缺陷检测成功");
        defectResultVO.setData(partInfo);
        int missionStatus = partInfo.getStatus() == PartStatus.NORMAL ? MissionStatus.NORMAL : MissionStatus.DEFECT;
        missions.forEach(m -> {
            if (m.getUid() == mission.getUid()) {
                m.setStatus(missionStatus);
            }
        });
        return defectResultVO;
    }
}
