package com.gylang.gim.web.process.controller;


import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.web.util.KeyLock;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.BaseSessionGroup;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.GimController;
import com.gylang.netty.sdk.api.provider.MessageProvider;
import com.gylang.netty.sdk.api.repo.GIMGroupSessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler(ChatType.CLIENT_AUTH)
@Component
public class TouristLoginHandler implements GimController<String> {

    @Resource
    private MessageProvider messageProvider;
    @Resource
    private GIMGroupSessionRepository groupSessionRepository;
    private static final KeyLock<String> keyLock = new KeyLock<>();

    @Override
    public Object process(GIMSession me, String requestBody) {

        me.setAccount(requestBody);
        BaseSessionGroup defaultGroup = getAndCreateGroup(me, groupSessionRepository);
        boolean join = groupSessionRepository.addMember(defaultGroup.getGroupId(), requestBody);
        if (join) {
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setSender(me.getAccount());
            messageWrap.setContent(requestBody + "加入群聊组");
            messageProvider.sendGroup(me, "1111", messageWrap);

            return messageWrap;
        }
        return null;
    }

    private BaseSessionGroup getAndCreateGroup(GIMSession me, GIMGroupSessionRepository groupRepository) {


        BaseSessionGroup defaultGroup;
        defaultGroup = groupRepository.findGroupInfo("111");
        if (null != defaultGroup) {
            return defaultGroup;
        }
        String key = "im:group_session:" + "default";
        try {
            keyLock.lock(key);
            // 前面的线程可以能已经创建完聊天组, 所以需要再次判断
            defaultGroup = groupRepository.findGroupInfo("111");
            if (null == defaultGroup) {
                // "default", me.getAccount(), 1000
                defaultGroup = new BaseSessionGroup();
                groupRepository.add(defaultGroup);
            }
        } finally {
            keyLock.unlock(key);
        }
        return defaultGroup;
    }
}
