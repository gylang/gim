package com.gylang.spring.netty.util;

import cn.hutool.core.collection.CollUtil;
import com.gylang.spring.netty.custom.method.ControllerMethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * 方法参数处理工具
 *
 * @author gylang
 * data 2020/11/27
 * @version v0.0.1
 */
public class MethodArgumentUtils {
    /**
     * 获取指定注解下参数
     *
     * @param controllerMethodMeta 方法元数据
     * @param annotation           注解类型
     * @param <T>                  注解类型
     * @return 注解实例
     */
    public static <T extends Annotation> MethodArgument getArgumentByAnnotation(ControllerMethodMeta controllerMethodMeta, Class<T> annotation) {

        Map<String, MethodArgument> argument = controllerMethodMeta.getArgument();
        if (CollUtil.isEmpty(argument)) {
            return null;
        }
        boolean hasTargetANnotation = false;
        for (MethodArgument methodArgument : argument.values()) {
            hasTargetANnotation = null != getAnnotationFormArgument(methodArgument, annotation);
            if (hasTargetANnotation) {
                return methodArgument;
            }
        }
        return null;
    }

    /**
     * 获取参数注解
     *
     * @param methodArgument 参数元数据
     * @param annotation     注解类型
     * @param <T>            注解类型
     * @return 注解实例
     */
    public static <T extends Annotation> T getAnnotationFormArgument(MethodArgument methodArgument, Class<T> annotation) {

        Set<Annotation> annotationList = methodArgument.getAnnotationList();
        for (Annotation anno : annotationList) {
            if (anno.annotationType().isAssignableFrom(annotation)) {
                return (T) anno;
            }
        }
        return null;

    }
}
