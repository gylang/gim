package com.gylang.gim.server.handle.client;


import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.event.EventProvider;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import com.gylang.netty.sdk.api.util.LocalSessionHolderUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler(ChatType.CLIENT_AUTH)
@Component
public class LoginHandler implements IMRequestHandler {

    @Resource
    private EventProvider eventProvider;
    @Resource
    private GIMSessionRepository sessionRepository;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Object process(GIMSession me, MessageWrap message) {

        MessageWrap messageWrap = ReplyMessage.success(message);
        messageWrap.setType(ChatType.REPLY_CHAT);
        // 获取用户信息
        if (StrUtil.isNotEmpty(me.getAccount())) {
            messageWrap.setContent("连接socket成功");
            messageWrap.setCode(BaseResultCode.OK.getCode());
            return messageWrap;
        }
        String uid = redisTemplate.opsForValue().get(message.getContent());
        if (null != uid) {
            // 用户已登录, 可以访问服务
            messageWrap.setMsg("连接socket成功");
            messageWrap.setCode(CommonConstant.TRUE_INT_STR);
            me.setAccount(uid);
            me.setStatus(GIMSession.ONLINE);
            sessionRepository.addSession(me);
            // 发送上线事件
            // bind 上下文
            LocalSessionHolderUtil.set(uid, me.getSession());
            eventProvider.sendEvent(EventTypeConst.USER_ONLINE, uid);
        } else {
            // token无效0
            messageWrap.setCode(BaseResultCode.USER_AUTH_BE_REJECTED.getCode());
            messageWrap.setMsg(BaseResultCode.USER_AUTH_BE_REJECTED.getMsg());
        }
        // 响应客户端
        return messageWrap;
    }


}
