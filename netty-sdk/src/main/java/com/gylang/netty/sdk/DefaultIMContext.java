package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.conveter.DataConverter;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gylang
 * data 2020/11/17
 */
@Getter
@Setter
public class DefaultIMContext implements IMContext {

    @NotNull
    private NotifyProvider notifyProvider;
    @NotNull
    private MessageProvider messageProvider;
    @NotNull
    private Object sessionRepository;
    @NotNull
    private Object groupRepository;
    @NotNull
    private DataConverter dataConverter;


    @Override
    public NotifyProvider notifyProvider() {
        return this.notifyProvider;
    }

    @Override
    public <T> T sessionRepository() {
        return (T) this.sessionRepository;
    }

    @Override
    public <T> T groupRepository() {
        return (T) this.groupRepository;
    }

    @Override
    public MessageProvider messageProvider() {
        return this.messageProvider;
    }

    @Override
    public DataConverter converter() {
        return this.dataConverter;
    }

    @Override
    public IMContext init(ImFactoryBuilder builder) {
        DefaultIMContext defaultIMContext = new DefaultIMContext();
        defaultIMContext.dataConverter = builder.getDataConverter();
        defaultIMContext.notifyProvider = builder.getNotifyProvider();
        defaultIMContext.sessionRepository = builder.getSessionRepository();
        defaultIMContext.groupRepository = builder.getGroupRepository();
        defaultIMContext.messageProvider = builder.getMessageProvider();
        return defaultIMContext;
    }

}
