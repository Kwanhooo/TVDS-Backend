package org.csu.tvds;

import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.common.PathConfig;
import org.csu.tvds.common.RuntimeConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;

@SpringBootApplication
@MapperScan("org.csu.tvds.persistence")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        initEnv();
        initBlobDirs();
    }

    /**
     * !请勿修改此方法!
     * 若要修改环境变量，请修改resources/env.yml文件
     */
    private static void initEnv() {
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(Application.class.getResourceAsStream("/env.yml"));
        if (load.get("os").equals("windows")) {
            PathConfig.AI_CODE_BASE = System.getProperty("user.dir") + "\\ai\\";
            PathConfig.BLOB_BASE = System.getProperty("user.dir") + "\\blob\\";
        }
        String pytorch = (String) load.get("pytorch");
        if (StringUtils.isNotBlank(pytorch)) {
            RuntimeConfig.TORCH_ENV = pytorch;
        }
        String tensorflow = (String) load.get("tensorflow");
        if (StringUtils.isNotBlank(tensorflow)) {
            RuntimeConfig.TENSORFLOW_ENV = tensorflow;
        }
    }

    private static void initBlobDirs() {
        System.out.println("初始化blob目录...");
        mkdirs(
                PathConfig.BLOB_BASE,
                PathConfig.ORIGIN_BASE,
                PathConfig.COMPOSITE_BASE,
                PathConfig.ALIGNED_BASE,
                PathConfig.MARKED_BASE,
                PathConfig.PARTS_BASE
        );
        System.out.println("初始化blob目录完成");
    }

    private static void mkdirs(String... dirs) {
        for (String str : dirs) {
            File dir = new File(str);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                System.out.println("创建目录" + dir.getAbsolutePath() + (mkdirs ? "成功" : "失败"));
            }
        }
    }
}
