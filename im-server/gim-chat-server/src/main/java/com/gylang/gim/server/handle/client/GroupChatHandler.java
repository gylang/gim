package com.gylang.gim.server.handle.client;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.GroupConfig;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(ChatTypeEnum.GROUP_CHAT)
public class GroupChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;
    @Resource
    private RedisTemplate<String, GroupConfig> redisTemplate;
    @Resource
    private EventProvider eventProvider;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 群关系检验
        Map<String, GroupConfig> config = redisTemplate.<String, GroupConfig>opsForHash().
                entries(CacheConstant.GROUP_CHAT_CONFIG + message.getReceive());
        GroupConfig senderConfig = config.get(message.getSender());
        if (null != senderConfig) {
            // 发送者是当前群聊用户
            if (senderConfig.isBanedSend()) {
                return ReplyMessage.reply(message, BaseResultCode.VISIT_INTERCEPT.getCode(), "用户被禁言");
            } else {
                // 发送入库事件
                message.setOfflineMsgEvent(false);
                String content = message.getContent();
                // 离线发送的消息体
                MessageWrap persistence = message.copyBasic();
                PushMessage pushMessage = new PushMessage();
                pushMessage.setReceiveId(config.keySet());
                pushMessage.setContent(content);
                persistence.setContent(JSON.toJSONString(pushMessage));
                eventProvider.sendEvent(EventTypeConst.PERSISTENCE_MSG_EVENT, persistence);
                // 发送群消息
                for (String account : config.keySet()) {
                    messageProvider.sendMsg(me, account, message.copyBasic());
                }

                return ReplyMessage.success(message);
            }
        }
        return ReplyMessage.reply(message, BaseResultCode.EMAIL_CODE_ERROR.getCode(), "非群成员");
    }
}
