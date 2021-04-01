package com.gylang.gim.im.biz;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import com.gylang.gim.im.constant.cmd.PrivateChatCmd;
import com.gylang.gim.im.constant.CommonConstant;
import com.gylang.gim.im.constant.EventType;
import com.gylang.gim.im.domain.ResponseMessageWrap;
import com.gylang.im.common.enums.BaseResultCode;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.constant.ChatTypeEnum;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler(PrivateChatCmd.LOGIN_SOCKET)
@Component
public class LoginHandler implements IMRequestHandler {

    @Resource
    private EventProvider eventProvider;
    @Resource
    private IMSessionRepository sessionRepository;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Object process(IMSession me, MessageWrap message) {
        MessageWrap messageWrap = new ResponseMessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setCmd(PrivateChatCmd.SOCKET_CONNECTED);
        messageWrap.setType(ChatTypeEnum.NOTIFY.getType());
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
            sessionRepository.add(uid, me);
            // 发送上线事件
            eventProvider.sendEvent(EventType.USER_ONLINE, uid);
            // bind 上下文
            LocalSessionHolderUtil.set(uid, me.getSession());
        } else {
            // token无效
            messageWrap.setContent("用户访问无权连接socket服务");
            messageWrap.setCode(CommonConstant.FALSE_INT_STR);
        }
        // 响应客户端
        return messageWrap;
    }
}
