package com.gylang.netty.sdk;

import com.gylang.netty.sdk.conveter.DataConverter;
import com.gylang.netty.sdk.factory.SimpleImFactory;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.sun.istack.internal.NotNull;

/**
 * im服务上下文实现类
 *
 * @author gylang
 * data 2020/11/8
 * @version v0.0.1
 */
public class DefaultImApplicationContext extends SimpleImFactory implements ImApplicationContext {

    @NotNull
    private IMSessionRepository sessionRepository;
    @NotNull
    private IMGroupSessionRepository groupRepository;
    @NotNull
    private DataConverter converter;
    @NotNull
    private MessageProvider messageProvider;


    @Override
    public void sendMsg(String key, Object message) {
        getNotifyProvider().sendMsg(key, message);
    }

    @Override
    public void sendAsyncMsg(String key, Object message) {
        getNotifyProvider().sendAsyncMsg(key, message);
    }

    public void doInit(ImFactoryBuilder builder) {

        // 初始化参数
        setNotifyProvider(builder.getNotifyProvider());
        setNettyConfig(builder.getNettyConfig());
        setRequestAdapter(builder.getDispatchAdapterHandler());
        setSessionRepository(builder.getSessionRepository());
        setGroupRepository(builder.getGroupRepository());
        setMessageProvider(builder.getMessageProvider());
        setConverter(builder.getDataConverter());
        // 初始化netty服务
        setImContext(new DefaultIMContext().init(builder));
        super.init();


    }

    public void setConverter(DataConverter converter) {
        this.converter = converter;
    }

    private void setSessionRepository(IMSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private void setGroupRepository(IMGroupSessionRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }
}
