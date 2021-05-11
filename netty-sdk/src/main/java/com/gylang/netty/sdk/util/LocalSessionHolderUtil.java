package com.gylang.netty.sdk.util;


import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gylang
 * data 2021/3/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalSessionHolderUtil {

    private static final Map<String, Channel> sessionContainer = new ConcurrentHashMap<>();

    /**
     * 获取本地 channel
     * @param key key
     * @return
     */
    public static Channel getSession(String key) {

        return sessionContainer.get(key);
    }

    /**
     * 设置本地channel
     * @param key key
     * @param session channel
     * @return channel
     */
    public static Channel set(String key, Channel session) {

        return sessionContainer.put(key, session);
    }

    /**
     * 出去channel
     * @param key key
     * @return channel
     */
    public static Channel remove(String key) {

        return sessionContainer.remove(key);
    }
}
