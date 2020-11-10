package com.gylang.netty.sdk.domain.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.proto.MessageWrapProto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 负责存储,发送和通知
 *
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
@Setter
@Getter
public abstract class AbstractSessionGroup {

    /**
     * 组名
     */
    private String name;
    /**
     * 创建者
     */
    private Long creator;
    /**
     * 组关键字
     */
    private String key;
    /**
     * 房间密码
     */
    private String password;

    /**
     * 队列
     */
    private BlockingQueue<IMSession> group;
    /**
     * 线程执行器
     */
    private EventExecutor executor;

    public AbstractSessionGroup(String name, Long creator, String password, Integer capacity) {
        this.name = name;
        this.creator = creator;
        this.key = IdUtil.fastUUID();
        group = new ArrayBlockingQueue<>(capacity);
    }

    public AbstractSessionGroup(String name, Long creator, Integer capacity) {
        this(name, creator, null, capacity);
    }

    /**
     * 加入组
     *
     * @param session 加入在者
     * @return 加入结果
     */
    public boolean join(IMSession session) {

        boolean offer = group.offer(session);
        if (offer) {
            notifyJoin(session);
        }
        return offer;
    }

    /**
     * 排队加入
     *
     * @param session 加入session
     * @param timeout 超时时间
     * @return 加入结果
     */
    public boolean waitJoin(IMSession session, long timeout) {

        boolean offer = false;
        try {
            offer = group.offer(session, timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (offer) {
            notifyJoin(session);
        }
        return offer;
    }

    /**
     * 退出群组
     *
     * @param session 加入会话
     * @return 加入结果
     */
    public boolean remove(IMSession session) {

        boolean offer = group.remove(session);
        if (offer) {
            notifyRemove(session);
        }
        return offer;
    }

    /**
     * 退出通知
     *
     * @param session 退出session
     */
    protected abstract void notifyRemove(IMSession session);


    /**
     * 加入消息推送
     *
     * @param imSession 触发着
     */
    protected abstract void notifyJoin(IMSession imSession);

    /**
     * 获取
     *
     * @param sessionId 发送接收者
     * @param message   消息主体
     */
    public void privateChat(String sessionId, MessageWrap message) {

        for (IMSession next : group) {
            if (sessionId.equals(next.getAccount())) {
                Channel session = next.getSession();
                if (session.isActive()) {
                    session.write(message).addListener(future -> {
                        notifyGroupChat(next, message, future);
                    });
                } else {
                    notifyPrivateChatNotOnline(next, message);
                }
            }
        }
    }

    /**
     * 获取
     *
     * @param sessionId 发送接收者
     * @param message   消息主体
     */
    public void groupChat(String sessionId, MessageWrap message) {

        for (IMSession next : group) {
            if (!sessionId.equals(next.getAccount())) {
                Channel session = next.getSession();
                if (session.isActive()) {
                    // todo 消息发送需要抽象处理 进行不同协议的适配问题 应该将初始化器 handler 和 进行统一封装处理

                    session.writeAndFlush(MessageWrapProto.Model.newBuilder()
                            .setContent(message.getContent())).addListener(future -> {
                        notifyGroupChat(next, message, future);
                    });
                } else {
                    notifyGroupChatNotOnline(next, message);
                }
            }
        }
    }

    /**
     * 群聊推送信息给某人不在线
     *
     * @param next    推送对象
     * @param message 消息
     */
    public abstract void notifyGroupChatNotOnline(IMSession next, MessageWrap message);

    /**
     * 群聊推送信息给某人成功
     *
     * @param next    推送对象
     * @param message 消息
     */
    public abstract void notifyGroupChat(IMSession next, MessageWrap message, Future<?> future);


    /**
     * 私聊发送成功通知
     *
     * @param next    私聊对象
     * @param message 发送消息
     */
    public abstract void notifyPrivateChat(IMSession next, MessageWrap message, ChannelFuture future);

    /**
     * 私聊不在线通知
     *
     * @param next    私聊对象
     * @param message 发送消息
     */
    public abstract void notifyPrivateChatNotOnline(IMSession next, MessageWrap message);

    public List<IMSession> getGroupMemberList() {
        return CollUtil.newArrayList(group);
    }
}
