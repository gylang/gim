package com.gylang.netty.sdk;

import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;

/**
 * 抽象im工厂
 *
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public interface AbstractImFactory {

    /**
     * 获取会话
     *
     * @param key key
     * @return 会话
     */
    IMSession getSession(String key);

    /**
     * 注册会话
     *
     * @param session 会话
     */
    void register(IMSession session);

    /**
     * 移除会话
     *
     * @param key 会话key
     */
    void remove(String key);

    /**
     * 加入群组
     *
     * @param group   群组id
     * @param session 加入的会话
     * @return 加入结果
     */
    boolean join(String group, IMSession session);

    /**
     * 关闭群组
     *
     * @param key 群组key
     * @return 关闭结果
     */
    boolean closeGroup(String key);

    /**
     * 退出群组
     *
     * @param key     群组key
     * @param session 退出群组的会话
     * @return 退出结果
     */
    boolean exitGroup(String key, IMSession session);

    /**
     * 构建群组
     *
     * @param group 构建群组参数
     * @return 构建的群组
     */
    AbstractSessionGroup createGroup(AbstractSessionGroup group);

    /**
     * 初始化工厂
     */
    void init();
}
