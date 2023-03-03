package org.csu.tvds;

import org.csu.tvds.entity.mysql.TemplatesLib;
import org.csu.tvds.service.TemplatesLibService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ORMTest {
    @Autowired
    private TemplatesLibService templatesLibService;

    @Test

    public void c() {
        TemplatesLib templatesLib = new TemplatesLib();
        templatesLib.setCameraNumber(1);
        templatesLib.setCreateTime(null);
        templatesLib.setCreateYear(2021);
        templatesLib.setId("1");
        templatesLib.setModel("model");
        templatesLib.setTemplateUrl("templateUrl");
        templatesLib.setVersion("version");
        templatesLibService.save(templatesLib);
    }

    @Test
    public void r() {
        TemplatesLib templatesLib = templatesLibService.getById("1");
        System.out.println(templatesLib);
    }

    @Test
    public void u() {
        TemplatesLib templatesLib = new TemplatesLib();
        templatesLib.setCameraNumber(1);
        templatesLib.setCreateTime(null);
        templatesLib.setCreateYear(2021);
        templatesLib.setId("1");
        templatesLib.setModel("3323");
        templatesLib.setTemplateUrl("2131");
        templatesLib.setVersion("11");
        templatesLibService.updateById(templatesLib);
    }

    @Test
    public void d() {
        templatesLibService.removeById("1");
    }


}
