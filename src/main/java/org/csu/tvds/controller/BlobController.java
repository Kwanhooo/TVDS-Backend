package org.csu.tvds.controller;


import org.apache.commons.io.FileUtils;
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
import java.util.Objects;

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
        String localPath = PathConfig.BLOB_BASE + path;
        System.out.println(System.getProperty("user.dir"));
        System.out.println(localPath);
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

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
}
