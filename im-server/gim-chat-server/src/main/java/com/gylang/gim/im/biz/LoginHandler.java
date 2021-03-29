package com.gylang.gim.im.biz;


import com.alibaba.fastjson.JSONObject;
import com.gylang.cache.CacheManager;
import com.gylang.gim.im.constant.cmd.PrivateChatCmd;
import com.gylang.gim.im.constant.CommonConstant;
import com.gylang.gim.im.constant.EventType;
import com.gylang.gim.im.domain.ResponseMessageWrap;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.IMRequestHandler;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
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
    private CacheManager cacheManager;


    @Override
    public Object process(IMSession me, MessageWrap message) {
        // 获取用户信息
        JSONObject userCache = cacheManager.get(message.getContent());
        String uid = null != userCache ? userCache.getString("id") : null;
        MessageWrap messageWrap = new ResponseMessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setCmd(PrivateChatCmd.SOCKET_CONNECTED);
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
