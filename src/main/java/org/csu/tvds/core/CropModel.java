package org.csu.tvds.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.csu.tvds.common.PathConfig;
import org.csu.tvds.core.annotation.CoreModel;
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

@CoreModel(env = TENSORFLOW_ENV)
@Slf4j
public class CropModel extends Model {
    private static final String OUTPUT_PATH = PathConfig.PARTS_BASE;
    private static final String JSON_PATH = AI_CODE_BASE + "tvds-registration/images/template/X70/part_index.json";

    {
        modelPath = AI_CODE_BASE + "tvds-registration/utils.py";
        template = new Template("{0} {1} {2} {3} {4}");
    }

    public Output<Boolean> dispatch(String imagePath) {
        Output<Boolean> output = new Output<>(null, false);

        File file = new File(imagePath);
        if (!file.exists()) {
            log.error("裁切前的参数检验 => 指定的图片路径不存在：" + imagePath);
            return new Output<>(null, false);
        }
        try {
            template.setValues(new String[]{TENSORFLOW_ENV, modelPath, imagePath, OUTPUT_PATH, JSON_PATH});
            String cmd = template.resolve();

            System.out.println(cmd);

            Process runtime = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = runtime.getInputStream();
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            if (lines.size() == 0) {
                output.setSucceed(false);
                output.setData(null);
                return output;
            }
            output.setSucceed(true);
            lines.forEach(line -> {
                if (line != null && line.trim().equals("True")) {
                    output.setData(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
