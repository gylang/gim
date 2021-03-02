package com.gylang.netty.sdk.util;

import com.gylang.netty.sdk.common.MethodWrap;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.common.ParameterWrap;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author gylang
 * data 2021/2/21
 */
public class ObjectWrapUtil {

    public static <T> T findAnnotation(Class<?> type, ObjectWrap objectWrap) {
        return findAnnotation(type, objectWrap.getAnnotationList());
    }

    public static <T> T findAnnotation(Class<?> type, MethodWrap objectWrap) {
        return findAnnotation(type, objectWrap.getAnnotationList());
    }

    public static <T> T findAnnotation(Class<?> type, ParameterWrap objectWrap) {
        return findAnnotation(type, objectWrap.getAnnotationList());
    }

    public static <T> T findAnnotation(Class<?> type, List<Annotation> annotationList) {
        if (null == annotationList || type == null) {
            return null;
        }
        for (Annotation annotation : annotationList) {
            if (type.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }
}
