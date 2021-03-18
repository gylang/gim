package com.gylang.im;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author gylang
 * data 2021/1/28
 */
@Component
@Slf4j
public abstract class AbstractIdemListener implements RocketMQListener<MessageExt> {

    @Resource
    private IdempotentService idempotentService;


    @Override
    public void onMessage(MessageExt message) {

        // 尝试进行幂等 如果幂等失败 不执行代码
        if (!idempotentService.teyIdempotent(getIdempotentKey(message), message)) {
            log.error("幂等校验失败: msgId = {}, topic = {}, body = {}",
                    message.getMsgId(), message.getTopic(), new String(message.getBody(), StandardCharsets.UTF_8));
            return;
        }
        try {
            // 业务消费消息
            doOnMessage(message);
        } catch (Exception e) {
            // 执行失败
            log.error("消息消费失败 : {}", e.getMessage());
            log.error("消息消费失败:  msgId = {}, topic = {}, body = {}",
                    message.getMsgId(), message.getTopic(), new String(message.getBody(), StandardCharsets.UTF_8));
            if (null != idempotentService) {
                idempotentService.errorProcess(getIdempotentKey(message), message);
            }
        }
    }

    protected abstract void doOnMessage(MessageExt message);

    protected abstract String getIdempotentKey(MessageExt message);


}
