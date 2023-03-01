package org.csu.inheritance.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author Kwanho
 * @date 2022-10-30 11:36
 * Mybatis-plus自带的字段填充
 */
@Configuration
public class MybatisMetaUtil implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("regTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("uploadTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("purchaseTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("fileUploadTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("uploadTime", LocalDateTime.now(), metaObject);
    }
}
