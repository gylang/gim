package com.gylang.spring.netty.custom.adapter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gylang.netty.sdk.annotation.AdapterType;
import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.BizRequestAdapter;
import com.gylang.netty.sdk.handler.IMRequestAdapter;
import com.gylang.spring.netty.annotation.SpringNettyController;
import com.gylang.spring.netty.custom.handler.MethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgumentValue;
import com.gylang.spring.netty.custom.reslove.MethodArgumentResolver;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Component
@AdapterType
@Slf4j
public class MethodHandlerAdapter implements BizRequestAdapter, InitializingBean {
    @Autowired
    private MethodArgumentResolver methodArgumentResolver;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MethodArgumentResolverAdapter methodArgumentResolverAdapter;

    private Map<String, MethodMeta> methodHandlerMap;

    @Override
    public void process(ChannelHandlerContext ctx, IMSession me, MessageWrap message, NotifyProvider messagePusher) {
        MethodMeta methodMeta = methodHandlerMap.get(message.getKey());
        if (null == methodMeta) {
            return;
        }
        MethodArgumentValue methodArgumentValue = new MethodArgumentValue();
        methodArgumentValue.init(methodMeta);
        methodArgumentResolverAdapter.handler(ctx, me, message, methodArgumentValue);
    }

    @Override
    public void register(List<?> processList) {

        List<MethodMeta> methodMetaList = getTargetList(processList);
        if (CollUtil.isEmpty(methodMetaList)) {
            methodHandlerMap = CollUtil.newHashMap();
        } else {
            methodHandlerMap = methodMetaList.stream()
                    .collect(Collectors.toMap(this::getBizKey, methodHandler -> methodHandler));
        }

    }

    private String getBizKey(MethodMeta methodMeta) {

        SpringNettyController nettyController = methodMeta.getNettyController();

        NettyMapping nettyMapping = methodMeta.getNettyMapping();
        return StrUtil.isBlank(nettyController.value()) ? nettyMapping.value() : nettyController.value() + ":" + nettyMapping.value();
    }

    @Override
    public List<?> mappingList() {
        return CollUtil.newArrayList(methodHandlerMap.values());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化方法处理器");
        Map<String, Object> controllerMap = applicationContext.getBeansWithAnnotation(SpringNettyController.class);
        List<MethodMeta> methodMetaList = new ArrayList<>();
        for (Object controller : controllerMap.values()) {
            Class<?> clazz = controller.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (methodArgumentResolver.support(method)) {
                    MethodMeta methodMeta = methodArgumentResolver.resolve(method);
                    methodMeta.fillInstance(controller);
                    methodMeta.fillClass(clazz);
                    methodMeta.setNettyController(clazz.getAnnotation(SpringNettyController.class));
                    methodMetaList.add(methodMeta);
                }
            }
        }
        register(methodMetaList);
    }
}
