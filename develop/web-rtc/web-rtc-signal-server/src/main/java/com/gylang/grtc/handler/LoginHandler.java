package com.gylang.grtc.handler;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.GIMSessionRepository;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler(ChatType.PRIVATE_CHAT)
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
        messageWrap.setSender(me.getAccount());
        messageWrap.setType(ChatType.NOTIFY_CHAT);
        // 获取用户信息
        if (StrUtil.isNotEmpty(me.getAccount())) {
            messageWrap.setContent("连接socket成功");
            messageWrap.setCode(BaseResultCode.OK.getCode());
            return messageWrap;
        }
        String jsonStr = redisTemplate.opsForValue().get(message.getContent());
        JSONObject userCache = JSONObject.parseObject(jsonStr);
        String uid = null != userCache ? userCache.getString("id") : null;

        if (null != uid) {
            // 用户已登录, 可以访问服务
            messageWrap.setContent("连接socket成功");
            messageWrap.setCode(CommonConstant.TRUE_INT_STR);
            me.setAccount(uid);
            sessionRepository.addSession(me);
            // 发送上线事件
            // bind 上下文
            LocalSessionHolderUtil.set(uid, me.getSession());
            eventProvider.sendEvent(EventTypeConst.USER_ONLINE, uid);
        } else {
            // token无效
            messageWrap.setContent("用户访问无权连接socket服务");
            messageWrap.setCode(CommonConstant.FALSE_INT_STR);
        }
        // 响应客户端
        return messageWrap;
    }


}
