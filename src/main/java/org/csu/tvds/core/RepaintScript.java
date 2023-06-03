package org.csu.tvds.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.csu.tvds.common.PathConfig;
import org.csu.tvds.core.io.Model;
import org.csu.tvds.core.io.Output;
import org.csu.tvds.core.io.Template;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.csu.tvds.common.PathConfig.AI_CODE_BASE;
import static org.csu.tvds.common.RuntimeConfig.TENSORFLOW_ENV;

//@CoreModel(env = TENSORFLOW_ENV)
@Slf4j
public class RepaintScript extends Model {
    private static final String ALIGNED_IMAGE_PATH = PathConfig.ALIGNED_BASE;
    private static final String OUTPUT_MARKED_PATH = PathConfig.MARKED_BASE;
    private static final String TEMPLATE_PATH = AI_CODE_BASE + "tvds-registration/images/template/{{MODEL}}/template.jpg";
    private static final String JSON_PATH = AI_CODE_BASE + "tvds-registration/images/template/{{MODEL}}/part_index.json";
    private static final String TTC_PATH = AI_CODE_BASE + "tvds-registration/simsun.ttc";

    {
        modelPath = AI_CODE_BASE + "tvds-registration/image_repaint.py";
        template = new Template("{0} {1} {2} {3} {4} {5} {6}");
    }

    public Output<String> dispatch(String alignedPath, String model, String statusString) {
        System.out.println("状态字符串 => " + statusString);
        File file = new File(alignedPath);
        if (!file.exists()) {
            log.error("配准前的参数检验 => 指定的图片路径不存在：" + alignedPath);
            return new Output<>(null, false);
        }
        Output<String> output = new Output<>();
        // 设置template模板参数
        String actualTemplatePath = TEMPLATE_PATH.replace("{{MODEL}}", model);
        String actualJsonPath = JSON_PATH.replace("{{MODEL}}", model);
        template.setValues(new String[]{
                TENSORFLOW_ENV, modelPath,
                alignedPath, actualJsonPath,
                OUTPUT_MARKED_PATH, TTC_PATH, statusString
        });
        String cmd = template.resolve();
        System.out.println("REPAINT => " + cmd);
        log.debug("正发起`重绘`调用：" + cmd);
        try {
            Process runtime = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = runtime.getInputStream();
            List<String> stdOut = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            if (stdOut.size() == 0) {
                log.error("`重绘`失败");
                output.setSucceed(false);
                return output;
            }
            stdOut.forEach(line -> {
                if (line != null && line.trim().equals("True")) {
                    output.setSucceed(true);
                }
            });
        } catch (IOException e) {
            log.error("`重绘`调用失败，模型运行时发生异常");
        }
        log.debug("`重绘`调用结束，结果为：" + output.isSucceed());
        return output;
    }
}
