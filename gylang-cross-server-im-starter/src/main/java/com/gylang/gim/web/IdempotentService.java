package com.gylang.gim.web;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author gylang
 * data 2021/1/28
 */
public interface IdempotentService {

    /**
     * 尝试进行幂等校验
     *
     * @param key        幂等key
     * @param messageExt 消息
     * @return 幂等结果
     */
    boolean teyIdempotent(String key, MessageExt messageExt);

    /**
     * 业务执行错误处理
     *
     * @param key     幂等key
     * @param message 消息体
     */
    void errorProcess(String key, MessageExt message);
}
