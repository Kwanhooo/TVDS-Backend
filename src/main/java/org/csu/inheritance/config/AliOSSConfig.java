package org.csu.inheritance.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.csu.inheritance.util.OssSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Kwanho
 * @date 2022-12-24 16:25
 */
@Configuration
@Data
public class AliOSSConfig {
    static {
        // 读取配置文件
        Yaml yaml = new Yaml();
        InputStream resourceAsStream = OssSigner.class.getClassLoader().getResourceAsStream("oss.yml");
        Map<String, Object> config = yaml.load(resourceAsStream);
        endpoint = (String) config.get("endpoint");
        accessKeyId = (String) config.get("accessKeyId");
        accessKeySecret = (String) config.get("accessKeySecret");
        bucketName = (String) config.get("bucketName");
        expireSeconds = (int) config.get("expireSeconds");

        host = (String) config.get("host");
        dir = (String) config.get("dir");
        expire = (int) config.get("expire");
        maxSize = (int) config.get("maxSize");
        callback = (String) config.get("callback");
        callbackTimeout = (int) config.get("callbackTimeout");
    }

    /**
     * 生成访问文件的签名
      */
    public static final String endpoint;
    public static final String accessKeyId;
    public static final String accessKeySecret;
    public static final String bucketName;
    public static final int expireSeconds;

    /**
     * 生成上传文件的signature和callback
     */
    public static final String host;
    public static final String dir;
    public static final int expire;
    public static final int maxSize;
    public static final String callback;
    public static final int callbackTimeout;

    @Bean
    public OSS ossClient(){
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
