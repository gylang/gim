package com.gylang.netty.sdk.api.initializer;

import com.gylang.netty.sdk.api.common.AfterConfigInitialize;
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

    /**
     * 初始化名称类型
     *
     * @return 初始化名称
     */
    public abstract String getName();

    /**
     * 端口
     */
    private Integer port;

}
