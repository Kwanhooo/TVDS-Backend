package org.csu.tvds.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.csu.tvds.core.annotation.CoreModel;
import org.csu.tvds.core.io.Model;
import org.csu.tvds.core.io.Output;
import org.csu.tvds.core.io.Template;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.csu.tvds.common.PathConfig.AI_CODE_BASE;
import static org.csu.tvds.common.RuntimeConfig.TENSORFLOW_ENV;

@Slf4j
@CoreModel(env = TENSORFLOW_ENV)
public class OCRModel extends Model {
    {
        modelPath = AI_CODE_BASE + "tvds-ocr/utils.py";
        template = new Template("{0} {1} {2}");
    }

    public Output<String> dispatch(String imagePath) {
        Output<String> output = new Output<>();
        template.setValues(new String[]{TENSORFLOW_ENV, modelPath, imagePath});
        String cmd = template.resolve();
        System.out.println("OCR => " + cmd);
        log.debug("正发起OCR调用：" + cmd);
        try {
            Process runtime = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = runtime.getInputStream();
            List<String> stdOut = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            if (stdOut.size() == 0) {
                log.error("OCR无法识别出型号");
                output.setSucceed(false);
                return output;
            }
            output.setSucceed(true);
            stdOut.forEach(line -> {
                if (line != null && line.trim().length() > 0) {
                    output.setData(line);
                }
            });
        } catch (IOException e) {
            log.error("OCR调用失败，模型运行时发生异常");
        }
        log.debug("OCR调用结束，结果为：" + output.getData());
        return output;
    }
}
