package com.gylang.netty.sdk.initializer;

import com.gylang.netty.sdk.common.AfterConfigInitialize;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gylang
 * data 2020/12/1
 */
@Setter
@Getter
public abstract class CustomInitializer<T extends Channel> extends ChannelInitializer<T> implements AfterConfigInitialize {

    public abstract String getName();

    private Integer port;

}
