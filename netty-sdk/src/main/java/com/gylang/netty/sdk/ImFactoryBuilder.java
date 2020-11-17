package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.NotifyContext;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.config.NettyConfig;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private NotifyContext messageContext;

    private NotifyProvider notifyProvider;

    private NettyConfig nettyConfig;

    private IMRequestAdapter dispatchAdapter;

    private MessageProvider messageProvider;

    private Object sessionRepository;

    private Object groupRepository;

    private DataConverter dataConverter;

}
