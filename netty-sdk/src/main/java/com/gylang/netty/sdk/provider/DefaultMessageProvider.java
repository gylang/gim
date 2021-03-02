package com.gylang.netty.sdk.provider;

import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/11/12
 * @version v0.0.1
 */
public class DefaultMessageProvider implements MessageProvider {

    private IMSessionRepository sessionRepository;
    private IMGroupSessionRepository groupSessionRepository;
    private ThreadPoolExecutor executor;


    @Override
    public void sendMsg(IMSession me, String target, MessageWrap message) {

        IMSession imSession = sessionRepository.find(target);
        sendMsg(me, imSession, message);
    }

    @Override
    public void sendAsyncMsg(IMSession me, String target, MessageWrap message) {
        executor.execute(() -> sendMsg(me, target, message));
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
    public void sendMsg(IMSession me, IMSession target, MessageWrap message) {
        if (null == target || null == target.getSession()) {
            return;
        }
        message.setSender(target.getAccount());
        target.getSession().writeAndFlush(message);
    }

    @Override
    public void sendAsyncMsg(IMSession me, IMSession target, MessageWrap message) {
        executor.execute(() -> sendMsg(me, target, message));
    }

    @Override
    public void sendGroup(IMSession me, AbstractSessionGroup target, MessageWrap message) {
        if (null == target) {
            return;
        }
        message.setSender(me.getAccount());
        for (IMSession session : target.getMemberList()) {
            if (null != session.getSession()
                    && !me.getNid().equals(session.getNid())
                    && session.getSession().isActive()) {
                session.getSession().writeAndFlush(message);
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
