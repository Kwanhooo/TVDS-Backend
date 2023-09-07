package org.csu.tvds;

import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.common.PathConfig;
import org.csu.tvds.common.RuntimeConfig;
import org.csu.tvds.cron.CompositeTimer;
import org.csu.tvds.cron.RepaintTimer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;

@SpringBootApplication
@MapperScan("org.csu.tvds.persistence")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        initEnv();
        initBlobDirs();
        initCron();

    }

    /**
     * !请勿修改此方法!
     * 若要修改环境变量，请修改resources/env.yml文件
     */
    private static void initEnv() {
        System.out.println("#1 初始化环境变量...");
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(Application.class.getResourceAsStream("/env.yml"));
        if (load.get("os").equals("windows")) {
            System.out.println("当前系统 => Windows");
            PathConfig.AI_CODE_BASE = System.getProperty("user.dir") + "\\ai\\";
            PathConfig.BLOB_BASE = System.getProperty("user.dir") + "\\blob\\";
        }
        String pytorch = (String) load.get("pytorch");
        if (StringUtils.isNotBlank(pytorch)) {
            RuntimeConfig.TORCH_ENV = pytorch;
            System.out.println("PyTorch位置 => " + RuntimeConfig.TORCH_ENV);
        }
        String tensorflow = (String) load.get("tensorflow");
        if (StringUtils.isNotBlank(tensorflow)) {
            RuntimeConfig.TENSORFLOW_ENV = tensorflow;
            System.out.println("TensorFlow位置 => " + RuntimeConfig.TENSORFLOW_ENV);
        }
        String vand = (String) load.get("vand");
        if (StringUtils.isNotBlank(vand)) {
            RuntimeConfig.VAND_ENV = vand;
            System.out.println("VAND位置 => " + RuntimeConfig.VAND_ENV);
        }
        System.out.println("#1* 初始化环境变量完成！");
    }

    public static void initBlobDirs() {
        System.out.println("#2 初始化blob目录...");
        mkdirs(
                PathConfig.BLOB_BASE,
                PathConfig.ORIGIN_BASE,
                PathConfig.COMPOSITE_BASE,
                PathConfig.ALIGNED_BASE,
                PathConfig.MARKED_BASE,
                PathConfig.PARTS_BASE,
                PathConfig.TEMP_BASE
        );
        System.out.println("#2* 初始化blob目录完成！");
    }

    private static void mkdirs(String... dirs) {
        for (String str : dirs) {
            File dir = new File(str);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                System.out.println("新建缺失目录 => " + dir.getAbsolutePath() + (mkdirs ? " 成功" : " 失败"));
            }
        }
    }

    private static void initCron() {
        System.out.println("#3 初始化定时任务...");
        CompositeTimer.start();
        RepaintTimer.start();
        System.out.println("#3* 初始化定时任务完成！");
    }
}
