package com.gylang.netty.sdk;

import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.GroupRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gylang
 * data 2020/11/12
 * @version v0.0.1
 */
@AllArgsConstructor
public class DefaultMessageProvider implements MessageProvider {

    private IMSessionRepository sessionRepository;
    private GroupRepository groupRepository;
    private ThreadPoolExecutor executor;
    private DataConverter converter;

    @Override
    public void sendMsg(IMSession me, String target, MessageWrap message) {

        IMSession imSession = sessionRepository.find(target);
        if (null == imSession || null == imSession.getSession()) {
            return;
        }
        message.setSender(imSession.getAccount());
        Object msg = converter.encode(message);
        imSession.getSession().writeAndFlush(msg);
    }

    @Override
    public void sendAsyncMsg(IMSession me, String target, MessageWrap message) {
        executor.execute(() -> sendMsg(me, target, message));
    }

    @Override
    public void sendGroup(IMSession me, String target, MessageWrap message) {

        AbstractSessionGroup sessionGroup = groupRepository.findByKey(target);
        if (null == sessionGroup) {
            return;
        }
        message.setSender(me.getAccount());
        for (IMSession session : sessionGroup.getMemberList()) {
            if (null != session.getSession() && !me.getNid().equals(session.getId())) {
                if (session.getSession().isActive()) {
                    Object msg = converter.encode(message);
                    session.getSession().writeAndFlush(msg);
                }
            }
        }
    }

    @Override
    public void sendAsyncGroup(IMSession me, String target, MessageWrap message) {
        executor.execute(() -> sendGroup(me, target, message));
    }
}
