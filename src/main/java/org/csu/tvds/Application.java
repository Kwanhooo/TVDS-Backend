package org.csu.tvds;

import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.common.PathConfig;
import org.csu.tvds.common.RuntimeConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@SpringBootApplication
@MapperScan("org.csu.tvds.persistence")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        initEnv();
    }

    private static void initEnv() {
        Yaml yaml = new Yaml();
        Map<String, String> load = yaml.load(Application.class.getResourceAsStream("/env.yml"));
        if (load.get("os").equals("windows")) {
            PathConfig.AI_CODE_BASE = System.getProperty("user.dir") + "\\ai\\";
            PathConfig.BLOB_BASE = System.getProperty("user.dir") + "\\blob\\";
        }
        String pytorch = load.get("pytorch");
        if (StringUtils.isNotBlank(pytorch)) {
            RuntimeConfig.TORCH_ENV = pytorch;
        }
        String tensorflow = load.get("tensorflow");
        if (StringUtils.isNotBlank(tensorflow)) {
            RuntimeConfig.TENSORFLOW_ENV = tensorflow;
        }
    }
}
