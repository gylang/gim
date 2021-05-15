package com.gylang.gim.cross;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.provider.CrossMessageProvider;

/**
 * @author gylang
 * data 2021/5/15
 */
public abstract class BaseCrossMessageProvider implements CrossMessageProvider {

    @Override
    public void receive(GIMSession sender, MessageWrap message) {

    }
}
