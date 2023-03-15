package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.OriginImage;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.OriginImageMapper;
import org.csu.tvds.util.SequenceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;

import static org.csu.tvds.common.CompositeAlignedImageStatus.COMPOSITE_FINISHED;

@Service
public class FileListenService {
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Resource
    private OriginImageMapper originImageMapper;

    public void handleOriginCreate(File file) {
        String fileName = file.getName();
        if (!fileName.contains(".jpg")) {
            return;
        }
        String inspectionSeqDay = file.getParentFile().getName();

        String[] split = fileName.split("\\.");
        String[] meta = split[0].split("_");
        String cameraNumber = meta[0];
        String carriageNo = meta[1];
        String serialNo = meta[2];

        OriginImage originImage = new OriginImage();
        originImage.setDbId(SequenceUtil.gen());
        originImage.setId(inspectionSeqDay + "_" + cameraNumber + "_" + carriageNo + "_" + serialNo);
        originImage.setFilename(fileName);
        originImage.setInspectionSeqDay(Integer.parseInt(inspectionSeqDay));
        originImage.setLocalUrl("origin/" + inspectionSeqDay + "/" + fileName);
        originImage.setCameraNumber(Integer.parseInt(cameraNumber));
        originImage.setCarriageNumber(Integer.parseInt(carriageNo));
        originImage.setContentType("image/jpeg");
        LocalDateTime now = LocalDateTime.now();
        originImage.setCreateTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0));
        originImage.setUpdateTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0));
        originImageMapper.insert(originImage);
        System.out.println("新的`原始图像`已入库：" + originImage);
    }

    public void handleCompositeCreate(File file) {
        String filename = file.getName();
        if (!filename.contains(".jpg")) {
            return;
        }
        String inspectionSeq = file.getParentFile().getName();

        String[] meta = filename.split("_");
        String createTimeAndNo = meta[0];
        int year = Integer.parseInt(createTimeAndNo.substring(0, 4));
        int month = Integer.parseInt(createTimeAndNo.substring(4, 6));
        int day = Integer.parseInt(createTimeAndNo.substring(6, 8));
        String cameraNo = meta[1];
        String carriageNo = meta[2].split("\\.")[0];

        CompositeAlignedImage targetImage = new CompositeAlignedImage();
        targetImage.setDbId(SequenceUtil.gen());
        targetImage.setId(inspectionSeq + "_" + filename.split("\\.")[0]);
        targetImage.setInspectionSeq(Integer.valueOf(inspectionSeq));
        targetImage.setCameraNumber(Integer.valueOf(cameraNo));
        targetImage.setCarriageNo(Integer.valueOf(carriageNo));
        targetImage.setStatus(COMPOSITE_FINISHED);
        targetImage.setCompositeTime(LocalDateTime.of(year, month, day, 0, 0, 0));
        targetImage.setCompositeUrl("composite/" + inspectionSeq + "/" + filename);
        targetImage.setCreateTime(LocalDateTime.now());
        targetImage.setUpdateTime(LocalDateTime.now());
        compositeAlignedImageMapper.insert(targetImage);
        System.out.println("新的`合成图像`已入库：" + targetImage);
    }

    public void handleCompositeDelete(File file) {
        String filename = file.getName();
        File parentFile = file.getParentFile();
        String inspectionSeq = parentFile.getName();
        String catalog = parentFile.getParentFile().getName();
        String dbUrl = catalog + "/" + inspectionSeq + "/" + filename;
        QueryWrapper<CompositeAlignedImage> wrapper = new QueryWrapper<>();
        wrapper.eq("compositeUrl", dbUrl);
        int delete = compositeAlignedImageMapper.delete(wrapper);
        System.out.println("已删除匹配的`合成图像`：" + dbUrl + "，删除数量：" + delete);
    }
}
