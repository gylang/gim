package com.gylang.netty.sdk.provider;

import cn.hutool.core.util.StrUtil;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.constant.qos.QosConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.api.config.GimGlobalConfiguration;
import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.event.EventProvider;
import com.gylang.netty.sdk.api.handler.qos.IMessageSenderQosHandler;
import com.gylang.netty.sdk.api.provider.CrossMessageProvider;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import com.gylang.netty.sdk.api.util.LocalSessionHolderUtil;
import com.gylang.netty.sdk.api.util.MsgIdUtil;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息发送类
 *
 * @author gylang
 * data 2020/11/12
 * @version v0.0.1
 */
@Slf4j
public class DefaultMessageProvider implements MessageProvider {
    /** 个人用户会话中心 */
    private GIMSessionRepository sessionRepository;
    /** 用户组会话中心 */
    private GIMGroupSessionRepository groupSessionRepository;
    /** 线程池 */
    private ThreadPoolExecutor executor;
    /** qos */
    private IMessageSenderQosHandler iMessageSenderQosHandler;
    /** 事件发送器 */
    private EventProvider eventProvider;
    private CrossMessageProvider crossMessageHandler;
    /** host */
    private String host = null;


    @Override
    public int sendMsg(GIMSession me, String target, MessageWrap message) {

        return sendMsgCallBack(me, target, message, null);
    }

    @Override
    public int sendMsg(GIMSession me, GIMSession target, MessageWrap message) {

        return sendMsgCallBack(me, target, message, null);
    }

    @Override
    public int sendMsgCallBack(GIMSession me, String target, MessageWrap message, GenericFutureListener<? extends Future<? super Void>> listener) {
        GIMSession gimSession = sessionRepository.findUserId(target);
        return sendMsgCallBack(me, gimSession, message, listener);
    }

    @Override
    public int sendMsgCallBack(GIMSession me, GIMSession target, MessageWrap message, GenericFutureListener<? extends Future<? super Void>> listener) {


        if (null == target) {
            eventProvider.sendEvent(EventTypeConst.USER_NOT_FOUND, message);
            return USER_NOT_FOUND;
        }
        // 设置接收者 channel
        if (null == target.getSession()) {
            target.setSession(LocalSessionHolderUtil.getSession(target.getAccount()));
        }
        // 发送策略 跨服传输 本地不存在当前channel 可以通过其他方式发送，桥接，mq
        if (!Objects.equals(host, target.getServerIp())) {
            // 跨服务消息 发送事件
            crossMessageHandler.sendMsg(host, me, message);
        }

        // 设置 发送者
        if (StrUtil.isEmpty(message.getSender())) {
            message.setSender(me.getAccount());
        }
        if (StrUtil.isEmpty(message.getReceive())) {
            message.setReceive(target.getAccount());
        }

        // 判断是否需要自动设置消息id
        if (StrUtil.isEmpty(message.getMsgId())) {
            message.setMsgId(MsgIdUtil.increase(message));
        }
        // 持久化消息
        if (message.isOfflineMsgEvent()) {
            eventProvider.sendEvent(EventTypeConst.PERSISTENCE_MSG_EVENT, message);
        }
        if (null == target.getSession()) {
            // 用户不在线
            return MessageProvider.USER_OFFLINE;
        }
        ChannelFuture cf = target.getSession().writeAndFlush(message);
        log.info("[发送消息] : 发送给: {}, 消息id = {}", target.getAccount(), message.getMsgId());
        if (log.isDebugEnabled()) {
            log.debug("[发送消息] : 消息内容 ： {}", message);
        }
        if (QosConstant.ONE_AWAY != message.getQos()) {
            // 应用层确保消息可达
            iMessageSenderQosHandler.addReceived(message);
        }
        cf.addListener(channelFuture -> {

            if (!channelFuture.isSuccess()) {
                // 消息发送失败 发送消息发送失败事件
                eventProvider.sendEvent(EventTypeConst.SEND_MES_ERROR, message);
            }

        });
        if (null != listener) {
            cf.addListener(listener);
        }

        return SENDING;
    }


    @Override
    public int sendGroup(GIMSession me, String target, MessageWrap message) {

        BaseSessionGroup sessionGroup = groupSessionRepository.findGroupInfo(target);
        return sendGroup(me, sessionGroup, message);
    }

    @Override
    public void sendAsyncGroup(GIMSession me, String target, MessageWrap message) {
        executor.execute(() -> sendGroup(me, target, message));
    }


    @Override
    public int sendGroup(GIMSession me, BaseSessionGroup target, MessageWrap message) {
        if (null == target) {
            return USER_NOT_FOUND;
        }
        message.setSender(me.getAccount());
        message.setReceive(target.getGroupId());
        Set<String> allMemberIds = groupSessionRepository.getAllMemberIds(target.getGroupId());
        for (GIMSession session : sessionRepository.findByUserIds(allMemberIds)) {
            if (null != session.getSession()
                    && !me.getNid().equals(session.getNid())
                    && session.getSession().isActive()) {
                sendMsg(me, session, message);
            }
        }
        return SENDING;
    }

    @Override
    public void sendAsyncGroup(GIMSession me, BaseSessionGroup target, MessageWrap message) {
        executor.execute(() -> sendGroup(me, target, message));
    }

    @Override
    public void init(GimGlobalConfiguration configuration) {
        this.sessionRepository = configuration.getSessionRepository();
        this.groupSessionRepository = configuration.getGroupSessionRepository();
        this.executor = configuration.getPoolExecutor();
        this.iMessageSenderQosHandler = configuration.getIMessageSenderQosHandler();
        this.eventProvider = configuration.getEventProvider();
        this.crossMessageHandler = configuration.getCrossMessageProvider();
        this.host = configuration.getProperties("serverId");
    }
}
