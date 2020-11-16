package com.gylang.netty.sdk.conveter;

import com.alibaba.fastjson.JSON;
import com.gylang.netty.sdk.domain.MessageWrap;

/**
 * json 数据转换
 *
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public class JsonConverter implements DataConverter {
    @Override
    public <T> T converterTo(Class<T> clazz, MessageWrap messageWrap) {
        String content = messageWrap.getContent();
        return JSON.parseObject(content, clazz);
    }

    @Override
    public <T, S> S encode(T object) {
        return (S) JSON.toJSONString(object);
    }
}
