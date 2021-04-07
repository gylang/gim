package com.gylang.gim.web.common.mybatis;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author gylang
 * data 2020/9/6
 * @version v0.0.1
 */
@ConditionalOnBean(GlobalConfig.class)
public class LogicDeleteConfig {

    @Bean
    public void initLogicDelete(@Autowired GlobalConfig globalConfig) {
        globalConfig.getDbConfig()
                .setLogicDeleteField("isDelete")
                .setLogicDeleteValue("1")
                .setLogicNotDeleteValue("0");
    }
}
