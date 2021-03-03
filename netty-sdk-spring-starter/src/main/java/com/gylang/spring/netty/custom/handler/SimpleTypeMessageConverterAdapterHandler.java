package com.gylang.spring.netty.custom.handler;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ClassUtil;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.spring.netty.custom.adapter.MessageConverterAdapter;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2020/11/27
 * @version v0.0.1
 */
@Component
public class SimpleTypeMessageConverterAdapterHandler implements MessageConverterAdapter<MessageWrap> {

    @Override
    public boolean support(MessageWrap wrap, Class<?> clazz) {
        return ClassUtil.isSimpleValueType(clazz);
    }

    @Override
    public <T> T resolve(MessageWrap wrap, Class<T> clazz) {
        if (null == wrap) {
            return null;
        }
        return ConverterRegistry.getInstance().convert(clazz, wrap);
    }
}
