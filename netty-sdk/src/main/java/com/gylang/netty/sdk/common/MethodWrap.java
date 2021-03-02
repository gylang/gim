package com.gylang.netty.sdk.common;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
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
