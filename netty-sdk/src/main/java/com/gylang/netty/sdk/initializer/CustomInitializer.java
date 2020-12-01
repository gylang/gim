package com.gylang.netty.sdk.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author gylang
 * data 2020/12/1
 */
public abstract class CustomInitializer<T extends Channel> extends ChannelInitializer<T> {
}
