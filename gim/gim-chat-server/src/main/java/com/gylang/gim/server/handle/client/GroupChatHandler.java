package com.gylang.gim.server.handle.client;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.server.service.SendAccessService;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.event.EventProvider;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author gylang
 * data 2021/3/5
 */
@Component
@NettyHandler(ChatType.GROUP_CHAT)
public class GroupChatHandler implements IMRequestHandler {
    @Resource
    private MessageProvider messageProvider;
    @Resource
    private SendAccessService sendAccessService;
    @Resource
    private EventProvider eventProvider;
    @Resource
    private GIMGroupSessionRepository groupSessionRepository;

    @Override
    public Object process(GIMSession me, MessageWrap message) {

        // 1. 群关系检验
        String receive = message.getReceive();
        boolean access = sendAccessService.privateAccessCheck(me.getAccount(), receive);

        // 发送消息
        if (access) {
            Set<String> memberIds = groupSessionRepository.getAllMemberIds(receive);
            if (!memberIds.contains(message.getSender())) {
                return ReplyMessage.reply(message, BaseResultCode.VISIT_INTERCEPT.getCode(), "非本群成员");
            }


            // 发送入库事件
            message.setOfflineMsgEvent(false);
            String content = message.getContent();
            // 离线发送的消息体
            MessageWrap persistence = message.copyBasic();
            PushMessage pushMessage = new PushMessage();
            pushMessage.setReceiveId(memberIds);
            pushMessage.setContent(content);
            persistence.setContent(JSON.toJSONString(pushMessage));
            eventProvider.sendEvent(EventTypeConst.PERSISTENCE_MSG_EVENT, persistence);
            // 发送群消息
            for (String account : memberIds) {
                messageProvider.sendMsg(me, account, message.copyBasic());
            }

            return ReplyMessage.success(message);


        }
        return ReplyMessage.reply(message, BaseResultCode.EMAIL_CODE_ERROR.getCode(), "非群成员");
    }
}
