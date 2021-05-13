package com.gylang.gim.web.process.controller;


import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.web.util.KeyLock;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.GIMSession;
import com.gylang.netty.sdk.handler.GimController;
import com.gylang.netty.sdk.provider.MessageProvider;
import com.gylang.netty.sdk.repo.IMGroupSessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler("login")
@Component
public class TouristLoginHandler implements GimController<String> {

    @Resource
    private MessageProvider messageProvider;
    @Resource
    private IMGroupSessionRepository groupSessionRepository;
    private static final KeyLock<String> keyLock = new KeyLock<>();

    @Override
    public Object process(GIMSession me, String requestBody) {

        me.setAccount(requestBody);
        AbstractSessionGroup defaultGroup = getAndCreateGroup(me, groupSessionRepository);
        boolean join = defaultGroup.join(me);
        if (join) {
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setSender(me.getAccount());
            messageWrap.setContent(requestBody + "加入群聊组");
            messageProvider.sendGroup(me, "1111", messageWrap);

        return messageWrap;
        }
        return null;
    }

    private AbstractSessionGroup getAndCreateGroup(GIMSession me, IMGroupSessionRepository groupRepository) {


        AbstractSessionGroup defaultGroup;
        defaultGroup = groupRepository.findByKey("111");
        if (null != defaultGroup) {
            return defaultGroup;
        }
        String key = "im:group_session:" + "default";
        try {
            keyLock.lock(key);
            // 前面的线程可以能已经创建完聊天组, 所以需要再次判断
            defaultGroup = groupRepository.findByKey("111");
            if (null == defaultGroup) {
                defaultGroup = new AbstractSessionGroup("default", me.getAccount(), 1000);
                groupRepository.add("1111", defaultGroup);
            }
        } finally {
            keyLock.unlock(key);
        }
        return defaultGroup;
    }
}