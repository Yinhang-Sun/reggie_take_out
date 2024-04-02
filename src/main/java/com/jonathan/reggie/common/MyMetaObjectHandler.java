package com.jonathan.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * Custom metadata object handler
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * autofill on insert operation
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Public fields automatically fill [insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     * autofill on update operation
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Public fields automatically fill [update]...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("Thread id is: {}", id);

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
