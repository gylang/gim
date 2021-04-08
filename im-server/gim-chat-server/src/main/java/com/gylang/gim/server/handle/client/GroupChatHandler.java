package com.gylang.gim.server.handle.client;

import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.cmd.GroupChatCmd;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.entity.GroupConfig;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.server.service.HistoryMessageService;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(GroupChatCmd.SIMPLE_GROUP_CHAT)
public class GroupChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;

    @Resource
    private HistoryMessageService messageService;
    @Resource
    private IMGroupSessionRepository groupSessionRepository;
    @Resource
    private RedisTemplate<String, GroupConfig> redisTemplate;

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
                // 缓存用户消息
                messageService.storeGroupChat(message.getReceive(), message);
                // 发送群消息
                AbstractSessionGroup group = groupSessionRepository.findByKey(message.getReceive());
                for (IMSession session : group.getMemberList()) {
                    messageProvider.sendMsg(me, session, message.copyBasic());
                }
                return ReplyMessage.success(message);
            }
        }
        return ReplyMessage.reply(message, BaseResultCode.EMAIL_CODE_ERROR.getCode(), "非群成员");
    }
}
