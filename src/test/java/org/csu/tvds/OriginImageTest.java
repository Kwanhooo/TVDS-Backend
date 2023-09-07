package org.csu.tvds;

import org.csu.tvds.entity.mysql.OriginImage;
import org.csu.tvds.persistence.mysql.OriginImageMapper;
import org.csu.tvds.util.SequenceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

@SpringBootTest
public class OriginImageTest {
    @Resource
    private OriginImageMapper originImageMapper;

    @Test
    public void save() {
        String path = "/home/kwanho/Workspace/Workspace-TVDS/TVDS-Backend/blob/origin/3907";
        File dir = new File(path);
        // 遍历文件夹，并输出每个文件的文件名
        // 2_1_0.jpg
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getName().contains(".jpg"))
                continue;
            String fileName = file.getName();
            System.out.println(fileName);
            String[] split = fileName.split("\\.");
            String[] meta = split[0].split("_");
            String inspectionSeqDay = "3907";
            String dateAndNo = "20220123001";
            String cameraNumber = meta[0];
            String carriageNo = meta[1];
            String serialNo = meta[2];

            OriginImage originImage = new OriginImage();
            originImage.setDbId(SequenceUtil.gen());
            originImage.setId(inspectionSeqDay + "_" + dateAndNo + "_" + cameraNumber + "_" + carriageNo + "_" + serialNo);
            originImage.setFilename(fileName);
            originImage.setInspectionSeqDay(inspectionSeqDay);
            originImage.setLocalUrl("origin/" + inspectionSeqDay + "/" + fileName);
            originImage.setCameraNumber(Integer.parseInt(cameraNumber));
            originImage.setCarriageNumber(Integer.parseInt(carriageNo));
            originImage.setContentType("image/jpeg");
            originImage.setCreateTime(LocalDateTime.of(2022, 1, 23, 0, 0, 0));
            originImage.setUpdateTime(LocalDateTime.of(2022, 1, 23, 0, 0, 0));
            originImageMapper.insert(originImage);
        }
    }
}
