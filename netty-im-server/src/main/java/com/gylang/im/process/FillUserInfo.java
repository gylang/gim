package com.gylang.im.process;

import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.FillUserInfoContext;
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
public class FillUserInfo implements FillUserInfoContext {

    @Resource(name = "imRepository")
    private DefaultIMRepository repository;
    @Resource
    private ApplicationContext applicationContext;


    @Override
    public void fill(IMSession session) {

        IMSession imSession = repository.find(session.getNid());
        if (null != imSession) {
        Channel channel = session.getSession();
        BeanUtils.copyProperties(imSession, session);
        session.setSession(channel);
        }
    }
}
