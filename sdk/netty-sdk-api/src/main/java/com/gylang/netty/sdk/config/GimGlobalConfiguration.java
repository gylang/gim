package com.gylang.netty.sdk.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.constant.GimDefaultConfigEnum;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.domain.CrossServerObserver;
import com.gylang.netty.sdk.event.EventContext;
import com.gylang.netty.sdk.event.EventProvider;
import com.gylang.netty.sdk.provider.CrossMessageProvider;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.IMessageRouter;
import com.gylang.netty.sdk.handler.qos.IMessageReceiveQosHandler;
import com.gylang.netty.sdk.handler.qos.IMessageSenderQosHandler;
import com.gylang.netty.sdk.initializer.CustomInitializer;
import com.gylang.netty.sdk.intercept.NettyIntercept;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.GIMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import com.gylang.netty.sdk.util.MsgIdUtil;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
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
public class GimGlobalConfiguration {


    /**
     * 根据名称装配，防止和客户端的ChannelInitializer冲突报错
     */
    private List<CustomInitializer<?>> serverChannelInitializer;
    /** 事件监听 */
    private EventProvider eventProvider;
    /** 事件上下文 */
    private EventContext eventContext;
    /** 数据类型转化 */
    private DataConverter dataConverter;
    /** 单用户会话工厂 */
    private GIMSessionRepository sessionRepository;
    /** 用户组会话工厂 */
    private IMGroupSessionRepository groupSessionRepository;
    /** 消息发送provider */
    private MessageProvider messageProvider;
    /** 事件监听列表 */
    private List<MessageEventListener<?>> messageEventListener;
    /** 业务请求适配器 */
    private List<BizRequestAdapter> bizRequestAdapterList;
    /** 消息路由分发器 */
    private IMessageRouter iMessageRouter;
    /** 线程池 */
    private ThreadPoolExecutor poolExecutor;
    /** 填充客户信息 */
    private NettyUserInfoFillHandler nettyUserInfoFillHandler;
    /** 消息处理拦截器 */
    private List<NettyIntercept> nettyInterceptList;
    /** 上下文可能使用到的对象包装类集合 主要用于适配业务调用的bean注入 */
    private List<ObjectWrap> objectWrapList;
    /** qos 发送处理器 */
    private IMessageReceiveQosHandler iMessageReceiveQosHandler;
    /** qos 接收 */
    private IMessageSenderQosHandler iMessageSenderQosHandler;

    /** 配置属性 */
    private GimProperties gimProperties;
    /** 消息id生成器 */
    private MsgIdUtil msgIdUtil = MsgIdUtil.getMsgId();
    /** 集群跨服通信观察对象 */
    private CrossMessageProvider crossMessageProvider;

    private CrossServerObserver crossServerObserver;
    /**
     * 配置信息
     */
    @Getter
    private Map<String, Object> properties;

    /**
     * 设置业务适配器
     * @param requestAdapterList 适配器列表
     */
    public void setBizRequestAdapterList(List<BizRequestAdapter> requestAdapterList) {
        if (CollUtil.isNotEmpty(requestAdapterList)) {

            this.bizRequestAdapterList = CollUtil.sort(requestAdapterList, requestAdapterList.get(0));
        } else {
            this.bizRequestAdapterList = requestAdapterList;
        }

    }

    /**
     * 设置配置参数
     * @param gimProperties 参数
     */
    public void setGimProperties(GimProperties gimProperties) {
        this.gimProperties = gimProperties;
        Map<String, Object> property = new HashMap<>();
        if (null != gimProperties.getProperties()) {
            property.putAll(gimProperties.getProperties());
        }
        BeanUtil.beanToMap(gimProperties, property, false, false);
        setProperties(property);
    }

    /**
     * 获取配置值
     *
     * @param key key
     * @return 配置值
     */
    public <T> T getProperties(String key) {
        return GimDefaultConfigEnum.getValue(key, properties);
    }

    /**
     * 获取配置值
     *
     * @param key key
     * @return 配置值
     */
    public <T> T getProperties(GimDefaultConfigEnum key) {
        return key.getValue(properties);
    }

    /**
     * 额外自定义对象 用户各种组件初始化场景中使用
     * @param objectWrapList 包装对象列表
     */
    public void addObjectWrap(List<ObjectWrap> objectWrapList) {
        if (CollUtil.isEmpty(objectWrapList)) {
            this.objectWrapList = new ArrayList<>(objectWrapList);
        }
        this.objectWrapList.addAll(objectWrapList);
    }

    /**
     * 额外自定义对象 用户各种组件初始化场景中使用
     * @param objectWrap 包装对象
     */
    public void addObjectWrap(ObjectWrap objectWrap) {
        if (null == objectWrapList) {
            this.objectWrapList = new ArrayList<>();
        }
        this.objectWrapList.add(objectWrap);
    }
    /**
     * 新增适配器
     * @param bizRequestAdapter 包装对象
     */
    public void addAdapter(BizRequestAdapter bizRequestAdapter) {

        if (null == bizRequestAdapterList) {
            bizRequestAdapterList = new ArrayList<>();
        }
        // 新增每次都会重新排序, 不会对影响性能
        bizRequestAdapterList.add(bizRequestAdapter);
        CollUtil.sort(bizRequestAdapterList, bizRequestAdapter);
    }
}
