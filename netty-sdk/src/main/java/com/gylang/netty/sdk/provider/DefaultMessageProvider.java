package com.gylang.netty.sdk.provider;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.util.MsgIdUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息发送类
 *
 * @author gylang
 * data 2020/11/12
 * @version v0.0.1
 */
public class DefaultMessageProvider implements MessageProvider {
    /** 个人用户会话中心 */
    private IMSessionRepository sessionRepository;
    /** 用户组会话中心 */
    private IMGroupSessionRepository groupSessionRepository;
    /** 线程池 */
    private ThreadPoolExecutor executor;


    @Override
    public void sendMsg(IMSession me, String target, MessageWrap message) {


        sendMsgCallBack(me, target, message, null);
    }

    @Override
    public void sendMsg(IMSession me, IMSession target, MessageWrap message) {

        sendMsgCallBack(me, target, message, null);
    }

    @Override
    public void sendMsgCallBack(IMSession me, String target, MessageWrap message, ChannelFutureListener listener) {
        IMSession imSession = sessionRepository.find(target);
        sendMsgCallBack(me, imSession, message, listener);
    }

    @Override
    public void sendMsgCallBack(IMSession me, IMSession target, MessageWrap message, ChannelFutureListener listener) {
        if (null == target || null == target.getSession()) {
            return;
        }
        message.setSender(me.getAccount());
        if (message.getReceive() <= 0) {
            message.setReceive(target.getAccount());
        }
        message.setMsgId(MsgIdUtil.increase(message.getType(), message.getReceive()));
        ChannelFuture cf = target.getSession().writeAndFlush(message);
        if (null != listener) {
            cf.addListener(listener);
        }
    }


    @Override
    public void sendGroup(IMSession me, String target, MessageWrap message) {

        AbstractSessionGroup sessionGroup = groupSessionRepository.findByKey(target);
        sendGroup(me, sessionGroup, message);
    }

    @Override
    public void sendAsyncGroup(IMSession me, String target, MessageWrap message) {
        executor.execute(() -> sendGroup(me, target, message));
    }


    @Override
    public void sendGroup(IMSession me, AbstractSessionGroup target, MessageWrap message) {
        if (null == target) {
            return;
        }
        message.setSender(me.getAccount());
        message.setReceive(target.getGroupId());
        for (IMSession session : target.getMemberList()) {
            if (null != session.getSession()
                    && !me.getNid().equals(session.getNid())
                    && session.getSession().isActive()) {
                sendMsg(me, session, message);
            }
        }
    }

    @Override
    public void sendAsyncGroup(IMSession me, AbstractSessionGroup target, MessageWrap message) {
        executor.execute(() -> sendGroup(me, target, message));

    }

    @Override
    public void init(NettyConfiguration configuration) {
        this.sessionRepository = configuration.getSessionRepository();
        this.groupSessionRepository = configuration.getGroupSessionRepository();
        this.executor = configuration.getPoolExecutor();
    }
}
