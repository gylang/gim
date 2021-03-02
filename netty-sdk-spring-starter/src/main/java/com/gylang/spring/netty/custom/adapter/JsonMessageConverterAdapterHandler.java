package com.gylang.spring.netty.custom.adapter;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.netty.sdk.domain.MessageWrap;
import org.springframework.stereotype.Component;

/**
 * json消息类型适配处理器
 *
 * @author gylang
 * data 2020/11/27
 * @version v0.0.1
 */
@Component
public class JsonMessageConverterAdapterHandler implements MessageConverterAdapter<MessageWrap> {

    @Override
    public boolean support(MessageWrap wrap, Class<?> clazz) {
        String content = wrap.getContent();
        return JSONUtil.isJson(content);
    }

    @Override
    public <T> T resolve(MessageWrap wrap, Class<T> clazz) {

        return JSON.parseObject(wrap.getContent(), clazz);
    }
}
