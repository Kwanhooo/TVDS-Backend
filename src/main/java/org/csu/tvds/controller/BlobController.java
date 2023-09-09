package org.csu.tvds.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.common.PathConfig;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.csu.tvds.common.PathConfig.AI_CODE_BASE;

@RestController
@RequestMapping("/blob")
public class BlobController {

    private static final Logger logger = Logger.getLogger(BlobController.class.getName());

    /**
     * 获取图片
     *
     * @param path 图片路径
     * @return 图片的字节数组
     */
    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(@RequestParam String path) {
        byte[] originalBytes = getFileBytes(path);

        try {
            byte[] compressedBytes = compressImage(originalBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(compressedBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "压缩图片时发生错误！", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 下载文件
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return 文件的字节数组
     */
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String path, @RequestParam(required = false) String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = path.substring(path.lastIndexOf("/") + 1);
        }
        byte[] bytes = getFileBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping("/template")
    public CommonResponse<?> template() {
        // 读取JSON文件
        String localPath = AI_CODE_BASE + "tvds-registration/images/template/X70/part_index.json";
        File fileToReturn = new File(localPath);
        try {
            List<String> lines = FileUtils.readLines(fileToReturn, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line);
            }
            return CommonResponse.createForSuccess(JSONObject.parseObject(sb.toString()));
        } catch (IOException e) {
            return CommonResponse.createForError("读取文件失败");
        }
    }

    private byte[] getFileBytes(String path) {
        String localPath = PathConfig.BLOB_BASE + path;
        System.out.println("索要图像 => " + localPath);
        File fileToReturn = new File(localPath);
        if (!fileToReturn.exists()) {
            return null;
        }
        byte[] bytes;
        try {
            bytes = FileUtils.readFileToByteArray(fileToReturn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    private byte[] compressImage(byte[] originalBytes) throws IOException {
        // 使用Thumbnailator进行图像压缩
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(originalBytes))
                .scale(0.5) // 调整图像大小，此处是原图大小的50%
                .outputQuality(0.8) // 设置图像质量，范围从0到1，1表示最佳质量
                .outputFormat("jpg") // 输出格式
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
}
