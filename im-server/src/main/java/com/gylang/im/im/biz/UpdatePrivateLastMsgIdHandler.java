package com.gylang.im.im.biz;

import com.gylang.cache.CacheManager;
import com.gylang.im.common.constant.CacheConstant;
import com.gylang.im.im.constant.BizChatCmd;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 更新个人的聊天最新消息msgId 用户手动拉取消息的时候方便进行
 *
 * @author gylang
 * data 2021/3/18
 */
@Component
@NettyHandler(BizChatCmd.PRIVATE_CHAT_LAST_MSG_ID)
public class UpdatePrivateLastMsgIdHandler implements IMRequestHandler {

    @Autowired
    private CacheManager cacheManager;

    @Value("${gylang.netty.privateMsgSlot:3}")
    private Integer slot;

    @Override
    public Object process(IMSession me, MessageWrap message) {

        // 用户量大 可以使用hash 先分组 再记录
        Long receive = message.getReceive();
        long targetSlot = receive & (slot - 1);
        cacheManager.setMapField(CacheConstant.PRIVATE_LAST_MSG_ID + targetSlot, String.valueOf(me.getAccount()), message.getContent());
        return null;
    }
}
