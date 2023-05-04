package org.csu.tvds;

import org.csu.tvds.entity.mysql.TemplatesLib;
import org.csu.tvds.persistence.mysql.TemplatesLibMapper;
import org.csu.tvds.util.SequenceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;

@SpringBootTest
public class TemplateLibTest {
    @Resource
    private TemplatesLibMapper templatesLibMapper;

    @Test
    public void buildLib() {
        TemplatesLib x70Wheel = new TemplatesLib();
        x70Wheel.setDbId(SequenceUtil.gen());
        x70Wheel.setId("x70Wheel_v1.0.0");
        x70Wheel.setModel("x70");
        TemplatesLib x70Bearing = new TemplatesLib();
        x70Bearing.setDbId(SequenceUtil.gen());
        x70Bearing.setId("x70Bearing_v1.0.0");
        x70Bearing.setModel("x70");
        TemplatesLib x70Spring = new TemplatesLib();
        x70Spring.setDbId(SequenceUtil.gen());
        x70Spring.setId("x70Spring_v1.0.0");
        x70Spring.setModel("x70");
        templatesLibMapper.insert(x70Wheel);
        templatesLibMapper.insert(x70Bearing);
        templatesLibMapper.insert(x70Spring);
    }

    @Test
    public void buildLibByDirs() {
        String baseTemplateDir = "/home/kwanho/Workspace/Workspace-TVDS/TVDS-Backend/ai/tvds-registration/images/template";
        File base = new File(baseTemplateDir);
        File[] files = base.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(f -> {
                // 如果是文件夹
                if (f.isDirectory()) {
                    String model = f.getName();
                    TemplatesLib wheel = new TemplatesLib();
                    wheel.setDbId(SequenceUtil.gen());
                    wheel.setId(model + "_wheel_v1");
                    wheel.setModel(model);
                    TemplatesLib bearing = new TemplatesLib();
                    bearing.setDbId(SequenceUtil.gen());
                    bearing.setId(model + "_bearing_v1");
                    bearing.setModel(model);
                    TemplatesLib spring = new TemplatesLib();
                    spring.setDbId(SequenceUtil.gen());
                    spring.setId(model + "_spring_v1");
                    spring.setModel(model);
                    templatesLibMapper.insert(wheel);
                    templatesLibMapper.insert(bearing);
                    templatesLibMapper.insert(spring);
                }
            });
        }
    }

    /**
     * 遍历baseDir下的所有文件夹内部的.jpg文件，并把他们的名字更改为template.jpg
     */
    @Test
    public void rename() {
        String baseDir = "/home/kwanho/Workspace/Workspace-TVDS/TVDS-Backend/ai/tvds-registration/images/template";
        File base = new File(baseDir);
        File[] files = base.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File[] subFiles = file.listFiles();
                    if (subFiles != null) {
                        for (File subFile : subFiles) {
                            if (subFile.getName().endsWith(".jpg")) {
                                subFile.renameTo(new File(subFile.getParent() + "/template.jpg"));
                            }
                        }
                    }
                }
            }
        }
    }
}
