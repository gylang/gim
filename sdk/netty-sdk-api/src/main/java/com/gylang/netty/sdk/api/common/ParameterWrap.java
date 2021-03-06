package com.gylang.netty.sdk.api.common;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数包装类 包装实例 参数名 参数类型 注解
 *
 * @author gylang
 * data 2021/2/21
 */
@Data
public class ParameterWrap {
    /** 实例 */
    private Object instance;
    /** 参数名 */
    private String name;
    /** 参数类型 */
    private Class<?> type;
    /** 注解 */
    private List<Annotation> annotationList;

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
