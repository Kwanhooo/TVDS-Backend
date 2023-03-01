package org.csu.inheritance.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.UploadFileRequest;
import org.csu.inheritance.config.AliOSSConfig;
import org.csu.inheritance.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class OssUploader {
    @Resource
    private OSS ossClient;

    /**
     * 上传某个文件到bucket的某个路径
     *
     * @param localPath 本地文件路径
     * @param ossPath   OSS文件路径
     * @throws BusinessException 上传失败
     * @author Kwanho
     */
    public void upload(String localPath, String ossPath) {
        // 1. 检查文件是否存在
        File file = new File(localPath);
        if (!file.exists()) {
            throw new BusinessException(1, "上传文件至OSS时出错——本地文件不存在", "上传文件至OSS时出错——本地文件不存在");
        }
        // 2. 设置meta
        ObjectMetadata meta = new ObjectMetadata();
        meta.setObjectAcl(CannedAccessControlList.Private); // ACL
        UploadFileRequest uploadFileRequest = new UploadFileRequest(AliOSSConfig.bucketName, ossPath);
        uploadFileRequest.setUploadFile(localPath);
        uploadFileRequest.setTaskNum(4); // 上传并发线程数
        uploadFileRequest.setPartSize(1024 * 1024); // 分片大小 (bit)
        uploadFileRequest.setEnableCheckpoint(true);// 断点续传
        uploadFileRequest.setObjectMetadata(meta); // 文件元信息
        // 3. 上传
        try {
            ossClient.uploadFile(uploadFileRequest);
        } catch (Throwable e) {
            throw new BusinessException(1, "上传文件至OSS时出错", "上传文件至OSS时出错——上传失败");
        }
    }

    /**
     * 上传某个文件到bucket的某个路径
     *
     * @param localFile 本地文件
     * @param ossPath   OSS文件路径
     * @throws BusinessException 上传失败
     * @author Kwanho
     */
    public void upload(File localFile, String ossPath) {
        upload(localFile.getAbsolutePath(), ossPath);
    }
}
