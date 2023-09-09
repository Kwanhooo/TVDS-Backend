package org.csu.tvds.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.csu.tvds.core.io.Model;
import org.csu.tvds.core.io.Output;
import org.csu.tvds.core.io.Template;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.csu.tvds.common.PathConfig.AI_CODE_BASE;
import static org.csu.tvds.common.PathConfig.OTHER_ASSETS_BASE;

@Slf4j
//@CoreModel(env = TENSORFLOW_ENV)
public class OCRModel extends Model {
    {
        modelPath = AI_CODE_BASE + "tvds-ocr/utils.py";
        template = new Template("{0} {1} {2}");
    }

//    public Output<String> dispatch(String imagePath) {
//        Output<String> output = new Output<>();
//        template.setValues(new String[]{TENSORFLOW_ENV, modelPath, imagePath});
//        String cmd = template.resolve();
//        System.out.println("OCR => " + cmd);
//        log.debug("正发起OCR调用：" + cmd);
//        try {
//            Process runtime = Runtime.getRuntime().exec(cmd);
//            InputStream inputStream = runtime.getInputStream();
//            List<String> stdOut = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
//            inputStream.close();
//            if (stdOut.size() == 0) {
//                log.error("OCR无法识别出型号");
//                output.setSucceed(false);
//                return output;
//            }
//            output.setSucceed(true);
//            stdOut.forEach(line -> {
//                if (line != null && line.trim().length() > 0) {
//                    output.setData(line);
//                }
//            });
//        } catch (IOException e) {
//            log.error("OCR调用失败，模型运行时发生异常");
//        }
//        System.out.println("OCR调用结束，结果为：" + output.getData());
//        return output;
//    }

    /**
     * 从record.csv取出信息
     *
     * @param imagePath
     * @return
     */
    public Output<String> dispatch(String imagePath) {
        System.out.println("imagePath => " + imagePath);
        Output<String> output = new Output<>();
        output.setSucceed(true);

        String inspectionSeq = "";
        String date = "";
        String cameraNumber = "";
        String carriageNo = "";
        String model = "";
        String carriageId = "";

        // 定义正则表达式模式，用于匹配inspectionSeq、date、cameraNumber和carriageNo
        Pattern pattern = Pattern.compile("/composite/([^/]+)/([^/]+)_(\\d+)_(\\d+)\\.jpg");
        Matcher matcher = pattern.matcher(imagePath);

        if (matcher.find()) {
            inspectionSeq = matcher.group(1);
            date = matcher.group(2).substring(0, 8);
            cameraNumber = matcher.group(3);
            carriageNo = matcher.group(4);

            // 输出提取的子串
            System.out.println("inspectionSeq: " + inspectionSeq);
            System.out.println("date: " + date);
            System.out.println("cameraNumber: " + cameraNumber);
            System.out.println("carriageNo: " + carriageNo);

            // 从CSV文件中查找对应的carriageId和model
            // ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(OTHER_ASSETS_BASE + "mock.csv");
            String csvFilePath = file.getAbsolutePath();

            // TODO: 删掉
            //inspectionSeq = "K80";
            //carriageNo = "2";

            try {
                FileReader reader = new FileReader(csvFilePath); // 请替换为实际的CSV文件路径
                CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

                int count = 0; // 记录匹配的记录数量

                for (CSVRecord record : csvParser) {
                    if (inspectionSeq.equals(record.get("inspectionSeq"))) {
                        count++;

                        if (count == Integer.parseInt(carriageNo)) {
                            carriageId = record.get("carriageId");
                            model = record.get("model");
                            break; // 找到匹配的记录后可以退出循环
                        }
                    }
                }

                csvParser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("未找到匹配的子串。");
        }

        StringBuilder sb = new StringBuilder()
                .append(inspectionSeq).append("_")
                .append(date).append("_")
                .append(cameraNumber).append("_")
                .append(carriageNo).append("_")
                .append(model).append("_")
                .append(carriageId);
        output.setData(sb.toString());
        return output;
    }

    private void findInfoByInspectionSeq(String inspectionSeq) {

    }
}
