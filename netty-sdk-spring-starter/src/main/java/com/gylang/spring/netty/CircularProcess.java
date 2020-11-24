package com.gylang.spring.netty;

import java.util.List;

/**
 * @author gylang
 * data 2020/11/22
 */
public interface CircularProcess {


    /**
     * 注入得bean
     * @return
     */
    List<Object> autowired();

    /**
     * 需要被注入得bean
     * @return
     */
    List<Object> injectBeanList();
}
