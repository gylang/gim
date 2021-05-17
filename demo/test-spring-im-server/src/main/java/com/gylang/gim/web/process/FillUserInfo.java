package com.gylang.gim.web.process;

import cn.hutool.core.util.StrUtil;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.repo.GIMSessionRepository;
import com.gylang.netty.sdk.api.repo.NettyUserInfoFillHandler;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@Component
public class FillUserInfo implements NettyUserInfoFillHandler {
    @Resource
    private GIMSessionRepository repository;



    @Override
    public void fill(GIMSession session) {
        if (StrUtil.isNotEmpty(session.getAccount())) {
            return;
        }
        GIMSession gimSession = repository.findUserId(session.getAccount());
        if (null != gimSession) {
        Channel channel = session.getSession();
        BeanUtils.copyProperties(gimSession, session);
        session.setSession(channel);
        }
    }
}
