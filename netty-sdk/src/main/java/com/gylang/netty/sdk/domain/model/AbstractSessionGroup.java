package com.gylang.netty.sdk.domain.model;

import cn.hutool.core.util.IdUtil;
import io.netty.util.concurrent.EventExecutor;
import lombok.Getter;
import lombok.Setter;

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
public class AbstractSessionGroup {

    /**
     * 组名
     */
    private String name;
    /**
     * 创建者
     */
    private String creator;
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
    private BlockingQueue<IMSession> memberList;
    /**
     * 线程执行器
     */
    private EventExecutor executor;

    public AbstractSessionGroup(String name, String creator, String password, Integer capacity) {
        this.name = name;
        this.creator = creator;
        this.key = IdUtil.fastUUID();
        memberList = new ArrayBlockingQueue<>(capacity);
    }

    public AbstractSessionGroup(String name, String creator, Integer capacity) {
        this(name, creator, null, capacity);
    }

    /**
     * 加入组
     *
     * @param session 加入在者
     * @return 加入结果
     */
    public boolean join(IMSession session) {

        boolean offer = memberList.offer(session);
        if (!offer) {
            return offer;
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
            offer = memberList.offer(session, timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        return memberList.remove(session);
    }


}
