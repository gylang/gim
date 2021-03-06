package com.gylang.netty.sdk.api.config;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 内置配置参数
 *
 * @author gylang
 * data 2021/3/3
 */
@Data
public class GimProperties {

    /** 服务器ip */
    private String serverIp;

    /** 接收连接线程组 */
    private Integer bossGroup;

    /** 工作线程组 */
    private Integer workerGroup;
    /** 读空闲 */
    private Long readerIdle;
    /** 写空闲 */
    private Long writeIdle;
    /** 度写空闲 */
    private Long allIdle;
    /** 断开重连次数 */
    private Integer lostConnectRetryNum;
    /** 服务端口号 */
    private Integer socketServerPort;

    /** 异常重发机制 */
    private Integer reSendMsg;

    private Map<String, Integer> socketType;

    /** 额外参数配置 */
    private Map<String, Object> properties;
    /** 非授权验证命令集 */
    private Set<Integer> nonAuthType;
}
