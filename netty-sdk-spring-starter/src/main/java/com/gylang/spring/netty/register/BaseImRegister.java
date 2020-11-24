package com.gylang.spring.netty.register;

import com.gylang.spring.netty.IMAutoConfigration;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author gylang
 * data 2020/11/22
 */
public interface BaseImRegister {

    /**
     * 注册初始化
     */
    void doInit(IMAutoConfigration configration, ConfigurableListableBeanFactory beanFactory);
}
