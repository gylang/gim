package com.gylang.im.im.biz;


import com.gylang.im.common.constant.CommonConstant;
import com.gylang.im.common.mybatis.UserHelper;
import com.gylang.im.im.constant.BizChatCmd;
import com.gylang.im.im.constant.EventType;
import com.gylang.im.web.dto.msg.ResponseMessageWrap;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.NettyController;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler(BizChatCmd.LOGIN_SOCKET)
@Component
public class LoginHandler implements NettyController<String> {

    @Resource
    private EventProvider eventProvider;
    @Resource
    private IMSessionRepository sessionRepository;
    @Resource
    private UserHelper userHelper;

    @Override
    public Object process(IMSession me, String token) {

        // 获取用户信息
        Long uid = userHelper.getUid(token);
        MessageWrap messageWrap = new ResponseMessageWrap();
        messageWrap.setSender(me.getAccount());
        messageWrap.setCmd(BizChatCmd.SOCKET_CONNECTED);
        if (null != uid) {
            // 用户已登录, 可以访问服务
            messageWrap.setContent("连接socket成功");
            messageWrap.setCode(CommonConstant.TRUE_INT_STR);
            me.setAccount(String.valueOf(uid));
            sessionRepository.add(String.valueOf(uid), me);
            // 发送上线事件
            eventProvider.sendEvent(EventType.USER_ONLINE, uid);
        } else {
            // token无效
            messageWrap.setContent("用户访问无权连接socket服务");
            messageWrap.setCode(CommonConstant.FALSE_INT_STR);
        }
        // 响应客户端
        return messageWrap;
    }


}
