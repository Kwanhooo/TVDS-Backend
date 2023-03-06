package org.csu.tvds;

import org.csu.tvds.entity.mysql.TemplatesLib;
import org.csu.tvds.persistence.mysql.TemplatesLibMapper;
import org.csu.tvds.util.SequenceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
}
