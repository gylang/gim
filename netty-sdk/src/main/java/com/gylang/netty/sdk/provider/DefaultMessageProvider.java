package com.gylang.netty.sdk.provider;

import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.netty.sdk.config.NettyConfiguration;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.handler.qos.IMessageSenderQosHandler;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.util.LocalSessionHolderUtil;
import com.gylang.netty.sdk.util.MsgIdUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.Objects;
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

    private IMessageSenderQosHandler iMessageSenderQosHandler;

    private EventProvider eventProvider;

    private String host = null;

    private Integer retryNum;

    @Override
    public int sendMsg(IMSession me, String target, MessageWrap message) {

        return sendMsgCallBack(me, target, message, null);
    }

    @Override
    public int sendMsg(IMSession me, IMSession target, MessageWrap message) {

        return sendMsgCallBack(me, target, message, null);
    }

    @Override
    public int sendMsgCallBack(IMSession me, String target, MessageWrap message, ChannelFutureListener listener) {
        IMSession imSession = sessionRepository.find(target);
        return sendMsgCallBack(me, imSession, message, listener);
    }

    @Override
    public int sendMsgCallBack(IMSession me, IMSession target, MessageWrap message, ChannelFutureListener listener) {

        // todo remove 不应该将消息离线耦合到单一发送服务
        // 接收者不存在/离线
//        if (null == target || null == target.getSession()) {
//            if (message.isOfflineMsgEvent()) {
//                eventProvider.sendEvent(EventTypeConst.OFFLINE_MSG_EVENT, message);
//            }
//            if (message.isPersistenceEvent()) {
//                eventProvider.sendEvent(EventTypeConst.PERSISTENCE_EVENT, message);
//            }
//            return;
//        }
        if (null == target) {
            eventProvider.sendEvent(EventTypeConst.USER_NOT_FOUND, message);
            return USER_NOT_FOUND;
        }
        if (null != target.getSession()) {
            target.setSession(LocalSessionHolderUtil.getSession(target.getAccount()));
        }
        message.setSender(null != me ? me.getAccount() : null);
        if (StrUtil.isEmpty(message.getReceive())) {
            message.setReceive(target.getAccount());
        }
        // 发送策略 如果本地发送失败（主要是跨服和用户离线）， 可以通过其他方式发送，桥接，mq
        if (!Objects.equals(host, target.getServerIp())) {
            // 跨服务消息 发送事件
            eventProvider.sendEvent(EventTypeConst.CROSS_SERVER_PUSH, message);
            return CROSS_SERVER;
        }

        // 判断是否需要自动设置消息id
        if (StrUtil.isNotEmpty(message.getMsgId())) {
            message.setMsgId(MsgIdUtil.increase(message));
        }
//        // 持久化消息
//        if (message.isPersistenceEvent()) {
//            eventProvider.sendEvent(EventTypeConst.PERSISTENCE_EVENT, message);
//        }
        ChannelFuture cf = target.getSession().writeAndFlush(message);

        cf.addListener((ChannelFutureListener) channelFuture -> {

            if (!channelFuture.isSuccess()) {
                if (message.isQos()) {
                    // 应用层确保消息可达
                    iMessageSenderQosHandler.handle(message, target);
                } else if (message.getRetryNum() > retryNum) {
                    // iMessageSenderQosHandler
                    if (message.isOfflineMsgEvent()) {
                        // 消息发送失败 发送消息发送失败事件
                        eventProvider.sendEvent(EventTypeConst.OFFLINE_MSG_EVENT, message);
                    }
                } else {
                    // 重发 可以设置定时器 重发
                    message.setRetryNum(retryNum + 1);
                    sendMsgCallBack(me, target, message, (ChannelFutureListener) channelFuture);
                }
            }

        });
        if (null != listener) {
            cf.addListener(listener);
        }

        return SENDING;
    }


    @Override
    public int sendGroup(IMSession me, String target, MessageWrap message) {

        AbstractSessionGroup sessionGroup = groupSessionRepository.findByKey(target);
        return sendGroup(me, sessionGroup, message);
    }

    @Override
    public void sendAsyncGroup(IMSession me, String target, MessageWrap message) {
        executor.execute(() -> sendGroup(me, target, message));
    }


    @Override
    public int sendGroup(IMSession me, AbstractSessionGroup target, MessageWrap message) {
        if (null == target) {
            return USER_NOT_FOUND;
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
        return SENDING;
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
        this.iMessageSenderQosHandler = configuration.getIMessageSenderQosHandler();
        this.eventProvider = configuration.getEventProvider();
        this.host = configuration.getProperties("serverId");
        this.retryNum = configuration.getProperties(NettyConfigEnum.LOST_CONNECT_RETRY_NUM);
    }
}
