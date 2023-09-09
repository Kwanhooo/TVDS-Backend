package org.csu.tvds.core;

import org.apache.commons.io.IOUtils;
import org.csu.tvds.common.RuntimeConfig;
import org.csu.tvds.core.io.Model;
import org.csu.tvds.core.io.Output;
import org.csu.tvds.core.io.Template;
import org.csu.tvds.entity.mysql.PartInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.csu.tvds.common.PathConfig.AI_CODE_BASE;

//@CoreModel(env = RuntimeConfig.TORCH_ENV)
public class DefectModelLegacy extends Model {
    private static final String BEARING_TCH = AI_CODE_BASE + "tvds-ad/model/bearing.tch";
    private static final String BEARING_NPY = AI_CODE_BASE + "tvds-ad/logs/bearing.npy";
    private static final String SPRING_TCH = AI_CODE_BASE + "tvds-ad/model/spring.tch";
    private static final String SPRING_NPY = AI_CODE_BASE + "tvds-ad/logs/spring.npy";
    private static final String CLAMP_TCH = AI_CODE_BASE + "tvds-ad/model/clamp.tch";
    private static final String CLAMP_NPY = AI_CODE_BASE + "tvds-ad/logs/clamp.npy";

    String MODEL_PATH;
    String NPY_PATH;

    {
        modelPath = AI_CODE_BASE + "tvds-ad/tvds-ad.py";
        template = new Template("{0} {1} {2} {3} {4}");
    }

    public Output<Boolean> dispatch(String inputImage, PartInfo meta) {
        Output<Boolean> output = new Output<>(null, false);
        String[] split = inputImage.split("/");
        String filename = split[split.length - 1];
        // TODO: 改了PartType的来源 字符串裁剪 -> 上层函数传入
        // String partType = filename.split("_")[0];
        String partType = meta.getPartName();

        System.out.println("零部件类型：" + partType);
        if (partType.equals("bearing")) {
            MODEL_PATH = BEARING_TCH;
            NPY_PATH = BEARING_NPY;
        } else if (partType.equals("spring")) {
            MODEL_PATH = SPRING_TCH;
            NPY_PATH = SPRING_NPY;
        } else if (partType.equals("clamp")) {
            MODEL_PATH = CLAMP_TCH;
            NPY_PATH = CLAMP_NPY;
        } else {
            // 如果没有匹配的部件类型
            System.err.println("不受支持的部件：" + partType);
            return output;
        }

        try {
            template.setValues(new String[]{RuntimeConfig.TORCH_ENV, modelPath, inputImage, MODEL_PATH, NPY_PATH});
            String cmd = template.resolve();
            System.out.println("DEFECT => " + cmd);
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
                if (line.contains("DEFECT")) {
                    output.setData(true);
                }
            });
            if (output.getData() == null) {
                output.setData(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
