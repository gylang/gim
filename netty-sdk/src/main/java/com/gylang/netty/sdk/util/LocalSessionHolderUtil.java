package com.gylang.netty.sdk.util;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gylang
 * data 2021/3/29
 */
public class LocalSessionHolderUtil {

    private static final Map<String, Channel> sessionContainer = new ConcurrentHashMap<>();


    public static Channel getSession(String key) {

        return sessionContainer.get(key);
    }

    public static Channel set(String key, Channel session) {

        return sessionContainer.put(key, session);
    }

    public static Channel remove(String key) {

        return sessionContainer.remove(key);
    }
}
