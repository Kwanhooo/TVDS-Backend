package org.csu.tvds.controller;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.common.PathConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.csu.tvds.common.PathConfig.AI_CODE_BASE;

@RestController
@RequestMapping("/blob")
public class BlobController {

    /**
     * 获取图片
     *
     * @param path 图片路径
     * @return 图片的字节数组
     */
    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(String path) {
        byte[] bytes = getFileBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private byte[] getFileBytes(String path) {
        String localPath = PathConfig.BLOB_BASE + path;
        System.out.println("filepath => " + localPath);
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

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(String path, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = path.substring(path.lastIndexOf("/") + 1);
        }
        byte[] bytes = getFileBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @Deprecated
    @RequestMapping("/getUrl")
    public CommonResponse<?> getUrl(String path) {
        System.out.println(path);
        String localPath = PathConfig.BLOB_BASE + path;
        File fileToReturn = new File(localPath);
        System.out.println(fileToReturn.getName());
        String targetPath = Objects.requireNonNull(BlobController.class.getResource("/static")).getPath();
        System.out.println(targetPath);
        // 把fileToReturn拷贝到classpath:/cache下
        try {
            FileUtils.copyFileToDirectory(fileToReturn, new File(targetPath));
            System.out.println("拷贝成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return CommonResponse.createForSuccess("http://10.0.0.100:8080/cache/" + fileToReturn.getName());
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
}
