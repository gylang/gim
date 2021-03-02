package com.gylang.netty.sdk.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.gylang.netty.sdk.common.MethodWrap;
import com.gylang.netty.sdk.common.ObjectWrap;
import com.gylang.netty.sdk.common.ParameterWrap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2021/2/21
 */
public class ObjectWrapUtil {

    public static <T> T findAnnotation(Class<?> type, ObjectWrap objectWrap) {
        return findAnnotation(type, objectWrap.getAnnotationList());
    }

    public static ObjectWrap resolver(Class<?> userType, Object instance) {

        ObjectWrap objectWrap = new ObjectWrap();
        objectWrap.setUserType(userType);
        objectWrap.setAnnotationList(CollUtil.newArrayList(AnnotationUtil.getAnnotations(userType, true)));
        objectWrap.setInstance(instance);
        List<MethodWrap> methodWrapList = new ArrayList<>();
        for (Method method : userType.getMethods()) {
            if (Object.class.equals(method.getDeclaringClass())) {
                continue;
            }
            MethodWrap methodWrap = new MethodWrap();
            methodWrap.setMethod(method);
            methodWrap.setObject(instance);
            methodWrap.setAnnotationList(CollUtil.newArrayList(AnnotationUtil.getAnnotations(method, true)));
            methodWrapList.add(methodWrap);
            Parameter[] parameters = method.getParameters();
            if (ArrayUtil.isEmpty(parameters)) {
                continue;
            }
            List<ParameterWrap> parameterWrapList = new ArrayList<>();
            for (Parameter parameter : parameters) {
                ParameterWrap parameterWrap = new ParameterWrap();
                parameterWrap.setType(parameter.getType());
                parameterWrap.setInstance(instance);
                parameterWrap.setName(parameter.getName());
                parameterWrap.setAnnotationList(CollUtil.newArrayList(AnnotationUtil.getAnnotations(parameter, true)));
                parameterWrapList.add(parameterWrap);
            }
            methodWrap.setParameterWrapList(parameterWrapList);
        }
        objectWrap.setMethodWrapList(methodWrapList);
        return objectWrap;
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
