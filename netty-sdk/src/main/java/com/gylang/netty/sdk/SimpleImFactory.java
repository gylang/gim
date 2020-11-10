package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.message.MessageNotify;
import com.gylang.netty.sdk.call.MessagePusher;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.repo.IRepository;

/**
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public class SimpleImFactory implements AbstractImFactory {

    private IRepository<Object, Object, IMSession> sessionRepository;
    private IRepository<Object, Object, AbstractSessionGroup> groupRepository;
    private MessagePusher messagePusher;
    private NettyConfig nettyConfig;
    private IMRequestAdapter requestAdapter;
    private MessageNotify<Object> messageNotify;
    private IMServer imServer;

    @Override
    public IMSession getSession(String key) {
        return sessionRepository.findByKey(key);
    }

    @Override
    public void register(IMSession session) {
        sessionRepository.add(session.getAccount(), session);
    }

    @Override
    public void remove(String key) {
        sessionRepository.popByKey(key);
    }

    @Override
    public boolean join(String group, IMSession session) {
        AbstractSessionGroup sessionGroup = groupRepository.findByKey(group);
        return sessionGroup.join(session);
    }

    @Override
    public boolean closeGroup(String key) {

        return null != groupRepository.popByKey(key);
    }

    @Override
    public boolean exitGroup(String key, IMSession session) {
        AbstractSessionGroup sessionGroup = groupRepository.findByKey(key);
        if (null == sessionGroup) {
            return false;
        }
        return sessionGroup.remove(session);
    }

    @Override
    public AbstractSessionGroup createGroup(AbstractSessionGroup group) {
        return groupRepository.add(group.getKey(), group);
    }

    @Override
    public void init() {
        // 启动netty服务
        imServer = new IMServer();
        imServer.setNettyConfig(nettyConfig);
        try {
            imServer.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IRepository<Object, Object, IMSession> getSessionRepository() {
        return sessionRepository;
    }

    public void setSessionRepository(IRepository<?, ?, IMSession> sessionRepository) {
        this.sessionRepository = (IRepository<Object, Object, IMSession>) sessionRepository;
    }

    public IRepository<Object, Object, AbstractSessionGroup> getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(IRepository<?, ?, AbstractSessionGroup> groupRepository) {
        this.groupRepository = (IRepository<Object, Object, AbstractSessionGroup>) groupRepository;
    }

    public MessagePusher getMessagePusher() {
        return messagePusher;
    }

    public void setMessagePusher(MessagePusher messagePusher) {
        this.messagePusher = messagePusher;
    }

    public NettyConfig getNettyConfig() {
        return nettyConfig;
    }

    public void setNettyConfig(NettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
    }

    public IMRequestAdapter getRequestAdapter() {
        return requestAdapter;
    }

    public void setRequestAdapter(IMRequestAdapter requestAdapter) {
        this.requestAdapter = requestAdapter;
    }

    public MessageNotify<Object> getMessageNotify() {
        return messageNotify;
    }

    public void setMessageNotify(MessageNotify<Object> messageNotify) {
        this.messageNotify = messageNotify;
    }

}
