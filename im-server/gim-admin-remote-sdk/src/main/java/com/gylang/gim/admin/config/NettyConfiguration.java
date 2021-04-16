package com.gylang.gim.admin.config;

import com.gylang.gim.admin.event.EventContext;
import com.gylang.gim.admin.event.EventProvider;
import com.gylang.gim.admin.event.MessageEventListener;
import com.gylang.gim.api.domain.common.MessageWrap;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * netty服务配置, 全局配置类
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Data
public class NettyConfiguration {


    /** 事件监听 */
    private EventProvider eventProvider;
    /** 事件上下文 */
    private EventContext eventContext;
    /** 事件监听列表 */
    private List<MessageEventListener<MessageWrap>> messageEventListener;
    /** 线程池 */
    private ThreadPoolExecutor poolExecutor;




}
