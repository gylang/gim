package com.gylang.spring.netty.custom.handler;

import com.gylang.netty.sdk.annotation.NettyMapping;
import com.gylang.spring.netty.annotation.SpringNettyController;
import com.gylang.spring.netty.custom.method.MethodArgument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gylang
 * data 2020/11/26
 * @version v0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MethodMeta {

    /**
     * 方法对象
     */
    private Method method;
    /**
     * 所属类
     */
    private Class<?> clazz;
    /**
     * 实例对象
     */
    private Object instance;
    /**
     * 参数列表, 用户反射赋值
     */
    private Map<String, MethodArgument> argument;
    /**
     * 方法注解列表
     */
    private Set<Annotation> annotationList;
    /**
     * 该方法所在controller
     */
    private SpringNettyController nettyController;
    /**
     * 该方法的所标识的处理器的ley
     */
    private NettyMapping nettyMapping;

    /**
     *
     */
    @Builder.Default
    private Map<String, String> cacheData = new ConcurrentHashMap<>();

    /**
     * 填充实例对象
     */
    public void fillInstance(Object object) {
        this.instance = object;
        if (null != argument) {
            for (MethodArgument methodArgument : argument.values()) {
                methodArgument.setInstance(object);
            }
        }
    }

    /**
     * 填充所处类
     */
    public void fillClass(Class<?> clazz) {
        this.clazz = clazz;
        if (null != argument) {
            for (MethodArgument methodArgument : argument.values()) {
                methodArgument.setClazz(clazz);
            }
        }
    }

    public void pushCache(String key, String value) {
        cacheData.put(key, value);
    }

    public String getCache(String key) {
        return cacheData.get(key);
    }

    public void removeCache(String key) {
        cacheData.remove(key);
    }

    public MethodArgument getArgument(String key) {

        return argument.get(key);
    }
}
