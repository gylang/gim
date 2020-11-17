package com.gylang.netty.sdk;

import com.gylang.netty.sdk.call.NotifyProvider;
import com.gylang.netty.sdk.conveter.DataConverter;

/**
 * @author gylang
 * data 2020/11/17
 */
public interface IMContext {

    NotifyProvider notifyProvider();

    <T> T sessionRepository();

    <T> T groupRepository();

    MessageProvider messageProvider();

    DataConverter converter();

    IMContext init(ImFactoryBuilder builder);
}
