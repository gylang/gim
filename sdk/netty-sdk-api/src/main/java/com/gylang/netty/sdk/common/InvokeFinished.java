package com.gylang.netty.sdk.common;

/**
 * 调用完成处理 adapter调用完服务后,需要返回 InvokeFinished对象, 标识调用命令服务已完成
 *
 * @author gylang
 * data 2021/2/21
 */
public final class InvokeFinished {

    /** 用于标识调用完成的填充对象 */
    public static final InvokeFinished NULL_OBJECT = new InvokeFinished();

    private InvokeFinished() {
    }

    public static InvokeFinished getInstance() {
        return NULL_OBJECT;
    }

    /**
     * 用户标识已完成调用 返回的包装类
     *
     * @return 调用失败
     */
    public static InvokeFinished finish() {
        return NULL_OBJECT;
    }

    /**
     * 调用成功, 如果有调用结果就使用调用结果, 没有调用结果就是使用包装类
     *
     * @param result 调用结果
     * @return 调用结果/填充包装类
     */
    public static Object finish(Object result) {
        return null == result ? InvokeFinished.getInstance() : result;
    }

}
