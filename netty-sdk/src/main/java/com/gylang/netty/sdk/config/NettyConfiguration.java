package com.gylang.netty.sdk.config;

import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.constant.NettyConfigEnum;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.DispatchAdapterHandler;
import com.gylang.netty.sdk.handler.qos.IMessageReceiveQosHandler;
import com.gylang.netty.sdk.handler.qos.IMessageSenderQosHandler;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * netty服务配置, 全局配置类
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Data
public class NettyConfiguration {


    /**
     * 根据名称装配，防止和客户端的ChannelInitializer冲突报错
     */
    private CustomInitializer<?> serverChannelInitializer;
    /** 事件监听 */
    private EventProvider eventProvider;
    /** 时间上下文 */
    private EventContext eventContext;
    /** 数据类型转化 */
    private DataConverter dataConverter;
    /** 单用户会话工厂 */
    private IMSessionRepository sessionRepository;
    /** 用户组会话工厂 */
    private IMGroupSessionRepository groupSessionRepository;
    /** 消息发送provider */
    private MessageProvider messageProvider;
    /** 事件监听列表 */
    private List<MessageEventListener<?>> messageEventListener;
    /** 业务请求适配器 */
    private List<BizRequestAdapter<?>> bizRequestAdapterList;
    /** 适配分发器 */
    private DispatchAdapterHandler dispatchAdapterHandler;
    /** 线程池 */
    private ThreadPoolExecutor poolExecutor;
    /** 填充客户信息 */
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;
    /** 消息处理拦截器 */
    private List<NettyIntercept> nettyInterceptList;
    /** 上下文可能使用到的对象包装类集合 主要用于适配业务调用的bean注入 */
    private List<ObjectWrap> objectWrapList;
    /** qos 发送 */
    private IMessageReceiveQosHandler iMessageReceiveQosHandler;
    /** qos 接收 */
    private IMessageSenderQosHandler iMessageSenderQosHandler;

    /** 配置属性 */
    private NettyProperties nettyProperties;
    /**
     * 配置信息
     */
    @Getter
    private Map<String, Object> properties;

    public void setBizRequestAdapterList(List<BizRequestAdapter<?>> requestAdapterList) {
        if (CollUtil.isNotEmpty(requestAdapterList)) {

            this.bizRequestAdapterList = CollUtil.sort(requestAdapterList, requestAdapterList.get(0));
        } else {
            this.bizRequestAdapterList = requestAdapterList;
        }

    }

    public void setNettyProperties(NettyProperties nettyProperties) {
        this.nettyProperties = nettyProperties;
        setProperties(nettyProperties.getProperties());
    }

    /**
     * 获取配置值
     *
     * @param key key
     * @return 配置值
     */
    public <T> T getProperties(String key) {
        return NettyConfigEnum.getValue(key, properties);
    }

    /**
     * 获取配置值
     *
     * @param key key
     * @return 配置值
     */
    public <T> T getProperties(NettyConfigEnum key) {
        return key.getValue(properties);
    }


    public void addObjectWrap(List<ObjectWrap> objectWrapList) {
        if (CollUtil.isEmpty(objectWrapList)) {
            this.objectWrapList = new ArrayList<>(objectWrapList);
        }
        this.objectWrapList.addAll(objectWrapList);
    }

    public void addObjectWrap(ObjectWrap objectWrap) {
        if (null == objectWrapList) {
            this.objectWrapList = new ArrayList<>();
        }
        this.objectWrapList.add(objectWrap);
    }

    public void addAdapter(BizRequestAdapter<?> bizRequestAdapter) {

        if (null == bizRequestAdapterList) {
            bizRequestAdapterList = new ArrayList<>();
        }
        // 新增每次都会重新排序, 不会对影响性能
        bizRequestAdapterList.add(bizRequestAdapter);
        CollUtil.sort(bizRequestAdapterList, bizRequestAdapter);
    }
}
