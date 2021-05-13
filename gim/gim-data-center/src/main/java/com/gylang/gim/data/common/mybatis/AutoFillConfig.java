package com.gylang.gim.data.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author gylang
 * data 2020/9/6
 * @version v0.0.1
 */
@Component
@Slf4j
public class AutoFillConfig implements MetaObjectHandler {

    @Autowired(required = false)
    private UserHelper userHelper;

    @Override
    public void insertFill(MetaObject metaObject) {

        // 字段填充
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        String userId = null;
        try {
            if (null != userHelper) {
                userId = userHelper.getUid();
            }
        } catch (Exception e) {
            log.info("用户未登录");
        }
        log.info("【数据填充】：填充创建人 id: {}", userId);
        this.strictInsertFill(metaObject, "createBy", String.class, userId);
        this.strictInsertFill(metaObject, "modifyBy", String.class, userId);
        this.strictInsertFill(metaObject, "isDelete", Integer.class, 0);
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        String userId = null;
        try {
            if (null != userHelper) {
                userId = userHelper.getUid();
            }
        } catch (Exception e) {
            log.info("用户未登录");
        }
        log.info("【数据填充】：填充创建人 id: {}", userId);
        this.strictInsertFill(metaObject, "modifyBy", String.class, userId);

    }


}
