package com.gylang.netty.sdk.api.common;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法包装类 包装 方法对象 实例 注解 参数
 *
 * @author gylang
 * data 2020/11/9
 * @version v0.0.1
 */
@Data
public class MethodWrap {

    /** 方法 */
    private Method method;
    /** 实例 */
    private Object object;
    /** 类注解 */
    private List<Annotation> annotationList;
    /** 参数 */
    private List<ParameterWrap> parameterWrapList;

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
