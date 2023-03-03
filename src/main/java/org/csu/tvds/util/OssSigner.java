package org.csu.tvds.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;
import org.csu.tvds.common.ResponseCode;
import org.csu.tvds.config.AliOSSConfig;
import org.csu.tvds.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.Date;

@Component
@Slf4j
public class OssSigner {

    @Resource
    private OSS ossClient;

    /**
     * 为指定的文件生成签名
     *
     * @param path 文件路径（不包含桶）
     * @return 签名后的URL
     * @author Kwanho
     */
    public String sign(@NotNull String path) {
        log.info("正在为文件 {} 生成签名", path);
        URL url;
        // 2. 判定是否存在此对象
        if (!ossClient.doesObjectExist(AliOSSConfig.bucketName, path)) {
            throw new BusinessException(ResponseCode.FILE_NOT_EXISTS);
        }
        try {
            // 3. 设置URL过期时间
            Date expiration = new Date(new Date().getTime() + AliOSSConfig.expireSeconds * 1000L);
            url = ossClient.generatePresignedUrl(AliOSSConfig.bucketName, path, expiration);
        } catch (OSSException | ClientException e) {
            throw new BusinessException(ResponseCode.FILE_SERVICE_ERROR);
        }
        // 4. 返回URL
        return url.toString();
    }
}
