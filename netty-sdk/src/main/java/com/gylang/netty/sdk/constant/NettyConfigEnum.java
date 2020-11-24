package com.gylang.netty.sdk.constant;

import io.netty.handler.logging.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Properties;

/**
 * 定义netty服务配置的默认值, 当自定义的默认值为空是, 取默认值
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Getter
@AllArgsConstructor
public enum NettyConfigEnum {
    /**
     * 用来监控tcp链接 指定线程数 默认是1 用默认即可
     */
    BOSS_GROUP("bossGroup", 1),
    WORKER_GROUP("workerGroup", 2),
    /**
     * 空闲检查时间
     */
    READER_IDLE("readerIdle", 10L),
    WRITE_IDLE("writeIdle", 10L),
    ALL_IDLE("allIdle", 10L),
    /**
     * 连接丢失重连次数
     */
    LOST_CONNECT_RETRY_NUM("lostConnectRetryNum", 5),
    /**
     * 日志级别
     */
    LOG_LEVEL("logLevel", LogLevel.INFO),
    /**
     * 默认socket端口
     */
    SOCKET_SERVER_PORT("socketServerPort", 46000),
    ;
    private final String name;
    private final Object value;

    /**
     * 获取配置值
     *
     * @param name key
     * @return 配置值
     */
    public static <T> T getValue(String name) {

        for (NettyConfigEnum propertiesConst : values()) {
            if (propertiesConst.getName().equals(name)) {
                return (T) propertiesConst.getValue();
            }
        }
        return null;
    }

    /**
     * 获取配置值
     *
     * @param name key
     * @return 配置值
     */
    public static <T> T getValue(String name, Map<String, Object> properties) {

        Object o = properties.get(name);
        if (null == o) {
            return getValue(name);
        }
        return (T) o;
    }

    /**
     * 获取配置值
     *
     * @param properties 自定义配置
     * @return 配置值
     */
    public <T> T getValue(Map<String, Object> properties) {
        if (null == properties) {
            return (T) this.value;
        }
        Object o = properties.get(this.name);
        if (null == o) {
            return (T) this.value;
        }
        return (T) o;
    }

}
