package com.gylang.gim.cross;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.provider.CrossMessageProvider;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * @author gylang
 * data 2021/5/15
 */
@RocketMQMessageListener(consumerGroup = "im-cross-msg", topic = "127.0.0.1")
public class RocketMqCrossMessageProvider implements CrossMessageProvider, RocketMQListener {
    @Override
    public void sendMsg(String host, GIMSession sender, MessageWrap message) {

    }

    @Override
    public void receive(GIMSession sender, MessageWrap message) {

    }

    @Override
    public void onMessage(Object message) {

    }
}
