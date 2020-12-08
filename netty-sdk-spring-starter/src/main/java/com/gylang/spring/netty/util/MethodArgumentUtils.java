package com.gylang.spring.netty.util;

import cn.hutool.core.collection.CollUtil;
import com.gylang.spring.netty.custom.method.MethodMeta;
import com.gylang.spring.netty.custom.method.MethodArgument;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author gylang
 * data 2020/11/27
 * @version v0.0.1
 */
public class MethodArgumentUtils {

    public static <T extends Annotation> MethodArgument getArgumentByAnnotation(MethodMeta methodMeta, Class<T> annotation) {
        Map<String, MethodArgument> argument = methodMeta.getArgument();
        if (CollUtil.isEmpty(argument)) {
            return null;
        }
        boolean hasNettyBodyAnnotation = false;
        for (MethodArgument methodArgument : argument.values()) {
            hasNettyBodyAnnotation = null != getAnnotationFormArgument(methodArgument, annotation);
            if (hasNettyBodyAnnotation) {
                return methodArgument;
            }
        }
        return null;
    }

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
