package com.gylang.gim.im.biz;

import com.gylang.cache.CacheManager;
import com.gylang.gim.im.constant.cmd.GroupChatCmd;
import com.gylang.gim.im.constant.cmd.PrivateChatCmd;
import com.gylang.gim.im.constant.CacheConstant;
import com.gylang.gim.im.domain.AckMessageWrap;
import com.gylang.gim.im.domain.GroupConfig;
import com.gylang.gim.im.service.HistoryMessageService;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.constant.system.SystemMessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
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
    private CacheManager cacheManager;
    @Resource
    private HistoryMessageService messageService;
    @Resource
    private IMGroupSessionRepository groupSessionRepository;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 1. 好友关系校验
        Map<String, GroupConfig> config = cacheManager.getMap(CacheConstant.GROUP_CHAT_CONFIG + message.getReceive());
        GroupConfig senderConfig = config.get(message.getSender());
        if (null != senderConfig) {
            // 发送者是当前群聊用户
            if (senderConfig.isBanedSend()) {
                AckMessageWrap messageWrap = new AckMessageWrap(message);
                messageWrap.setCmd(SystemMessageType.ERROR_MSG);
                messageWrap.setContent("你已被禁言!");
                return messageWrap;
            } else {
                // 缓存用户消息
                messageService.storeGroupChat(message.getReceive(), message);
                // 发送群消息
                AbstractSessionGroup group = groupSessionRepository.findByKey(message.getReceive());
                for (IMSession session : group.getMemberList()) {
                    messageProvider.sendMsg(me, session, message.copyBasic());
                }
            }
        }
        return null;
    }
}
