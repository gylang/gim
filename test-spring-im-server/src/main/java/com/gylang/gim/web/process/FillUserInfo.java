package com.gylang.gim.web.process;

import cn.hutool.core.util.StrUtil;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.repo.IMSessionRepository;
import com.gylang.netty.sdk.repo.NettyUserInfoFillHandler;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@Component
public class FillUserInfo implements NettyUserInfoFillHandler {
    @Resource
    private IMSessionRepository repository;
    @Resource
    private ApplicationContext applicationContext;


    @Override
    public void fill(GIMSession session) {
        if (StrUtil.isNotEmpty(session.getAccount())) {
            return;
        }
        GIMSession gimSession = repository.find(session.getAccount());
        if (null != gimSession) {
        Channel channel = session.getSession();
        BeanUtils.copyProperties(gimSession, session);
        session.setSession(channel);
        }
    }
}
