package com.gylang.netty.sdk.common;

/**
 * @author gylang
 * data 2021/2/21
 */
public class InokeFinished {
    public static final InokeFinished NULL_OBJECT = new InokeFinished();
    private InokeFinished() {}

    public static InokeFinished getInstance() {
        return NULL_OBJECT;
    }

    /**
     * 用户标识已完成调用 返回的包装类
     * @return 调用失败
     */
    public static InokeFinished finish() {
        return NULL_OBJECT;
    }

    /**
     * 调用成功, 如果有调用结果就使用调用结果, 没有调用结果就是使用包装类
     * @param result 调用结果
     * @return 调用结果/填充包装类
     */
    public static Object finish(Object result) {
        return null == result ? InokeFinished.getInstance() : result;
    }

}
