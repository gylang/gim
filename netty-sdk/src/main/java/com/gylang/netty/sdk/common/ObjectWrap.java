package com.gylang.netty.sdk.common;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2021/2/21
 */
@Data
public class ObjectWrap {
    /** 实例 */
    private Object instance;

    private Class<?> userType;

    /** 类注解 */
    private List<Annotation> annotationList;
    /** 方法 */
    private List<MethodWrap> methodWrapList;

    public void addAnno(Annotation annotation) {
        if (null == annotation) {
            return;
        }
        if (null == annotationList) {
            annotationList = new ArrayList<>();
        }
        annotationList.add(annotation);
    }
}
