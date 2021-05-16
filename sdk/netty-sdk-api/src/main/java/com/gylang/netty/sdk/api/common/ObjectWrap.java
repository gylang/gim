package com.gylang.netty.sdk.api.common;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象包装类 包装 实例 源类型(非代理类) 注解 方法
 * @author gylang
 * data 2021/2/21
 */
@Data
public class ObjectWrap {
    /** 实例 */
    private Object instance;
    /** 源类型 */
    private Class<?> userType;

    /** 类注解 */
    private List<Annotation> annotationList;
    /** 方法 */
    private List<MethodWrap> methodWrapList;

    /**
     * 添加注解
     * @param annotation 注解
     */
    public void addAnnotation(Annotation annotation) {
        if (null == annotation) {
            return;
        }
        if (null == annotationList) {
            annotationList = new ArrayList<>();
        }
        annotationList.add(annotation);
    }
}
