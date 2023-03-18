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
import static org.csu.tvds.common.RuntimeConfig.TORCH_ENV;

@Slf4j
public class CompositeModel extends Model {
    private static final String TARGET_PATH = PathConfig.ORIGIN_BASE;
    private static final String TEMP_PATH = PathConfig.TEMP_BASE;
    private static final String SAVE_PATH = PathConfig.COMPOSITE_BASE;

    {
        modelPath = AI_CODE_BASE + "tvds-composite/resturct_multi2.py";
        template = new Template("{0} {1} {2} {3} {4}");
    }

    public Output<String> dispatch() {
        File file = new File(TARGET_PATH);
        if (!file.exists()) {
            log.error("合成前的参数检验 => 指定的图片路径不存在：" + TARGET_PATH);
            return new Output<>(null, false);
        }
        Output<String> output = new Output<>();
        template.setValues(new String[]{
                TORCH_ENV, modelPath, TARGET_PATH, TEMP_PATH, SAVE_PATH
        });
        String cmd = template.resolve();
        System.out.println("COMPOSITE => " + cmd);
        log.debug("正发起`合成`调用：" + cmd);
        try {
            Process runtime = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = runtime.getInputStream();
            List<String> stdOut = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            if (stdOut.size() == 0) {
                log.error("`合成`未产生任何结果");
                output.setSucceed(false);
            } else {
                output.setSucceed(true);
            }
        } catch (IOException e) {
            log.error("`合成`调用失败，模型运行时发生异常");
        }
        log.debug("合成调用结束，结果为：" + output.isSucceed());
        return output;
    }
}
