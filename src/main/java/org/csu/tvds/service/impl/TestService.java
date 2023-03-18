package org.csu.tvds.service.impl;

import org.csu.tvds.common.PathConfig;
import org.csu.tvds.common.ResponseCode;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.csu.tvds.util.SequenceUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;

import static org.csu.tvds.common.CompositeAlignedImageStatus.COMPOSITE_FINISHED;

@Service
public class TestService {
    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;

    @Resource
    private PartInfoMapper partInfoMapper;

    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;


    public CompositeAlignedImage handleCarriageUpload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        // FORMAT: inspectionSeq_20230103xxx_cameraNo_carriageNo.jpg
        String[] meta = filename.split("_");
        if (meta.length < 4) {
            throw new BusinessException(ResponseCode.ARGUMENT_ILLEGAL);
        }
        String inspectionSeq = meta[0];
        String createTimeAndNo = meta[1];
        int year = Integer.parseInt(createTimeAndNo.substring(0, 4));
        int month = Integer.parseInt(createTimeAndNo.substring(4, 6));
        int day = Integer.parseInt(createTimeAndNo.substring(6, 8));
        String cameraNo = meta[2];
        String carriageNo = meta[3].split("\\.")[0];
        // 扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        // 生成新的文件名
        String newFilename = createTimeAndNo + "_" + cameraNo + "_" + carriageNo + extension;
        // inspection这一目录如果不存在，则创建
        File inspectionDir = new File(PathConfig.COMPOSITE_BASE + inspectionSeq);
        if (!inspectionDir.exists()) {
            boolean mkdir = inspectionDir.mkdirs();
            if (!mkdir) {
                throw new BusinessException(ResponseCode.FILE_SERVICE_ERROR);
            }
        }
        // 保存文件
        File dest = new File(PathConfig.COMPOSITE_BASE + inspectionSeq + "/" + newFilename);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.FILE_SERVICE_ERROR);
        }
        // 保存到数据库
        CompositeAlignedImage targetImage = new CompositeAlignedImage();
        targetImage.setDbId(SequenceUtil.gen());
        targetImage.setId(filename.split("\\.")[0]);
        targetImage.setInspectionSeq(Integer.valueOf(inspectionSeq));
        targetImage.setCameraNumber(Integer.valueOf(cameraNo));
        targetImage.setCarriageNo(Integer.valueOf(carriageNo));
        targetImage.setStatus(COMPOSITE_FINISHED);
        targetImage.setCompositeTime(LocalDateTime.of(year, month, day, 0, 0, 0));
        targetImage.setCompositeUrl("composite/" + inspectionSeq + "/" + newFilename);
        targetImage.setCreateTime(LocalDateTime.now());
        targetImage.setUpdateTime(LocalDateTime.now());
        compositeAlignedImageService.save(targetImage);
        return targetImage;
    }

    public void rollback() {
        // 1. 清空零件信息表
        partInfoMapper.delete(null);
        // 2. 将合成对齐图像表中的status字段置为0
        compositeAlignedImageMapper.rollbackStatus();
    }
}
