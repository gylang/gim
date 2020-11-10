package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.MessageContext;
import com.gylang.netty.sdk.call.MessagePusher;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.netty.sdk.repo.GroupRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

/**
 * im服务建造者 用于构建im服务所需参数
 *
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImFactoryBuilder {

    private MessageContext messageContext;

    private MessagePusher messagePusher;

    private IMSessionRepository sessionRepository;

    private GroupRepository groupRepository;

    private NettyConfig nettyConfig;

    private IMRequestAdapter requestAdapter;

}
