package com.gylang.im.process;

import com.gylang.netty.sdk.IMContext;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.repo.DefaultIMRepository;
import com.gylang.netty.sdk.repo.FillUserInfoContext;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2020/11/17
 */
@Component
public class FillUserInfo implements FillUserInfoContext {

    @Autowired
    private IMContext context;


    @Override
    public void fill(IMSession session) {

        DefaultIMRepository repository = context.sessionRepository();
        IMSession imSession = repository.find(session.getNid());
        if (null != imSession) {
        Channel channel = session.getSession();
        BeanUtils.copyProperties(imSession, session);
        session.setSession(channel);
        }
    }
}
