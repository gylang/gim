package com.gylang.im.process.controller;


import com.gylang.im.util.KeyLock;
import com.gylang.netty.sdk.IMContext;
import com.gylang.netty.sdk.MessageProvider;
import com.gylang.netty.sdk.annotation.NettyHandler;
import com.gylang.netty.sdk.domain.MessageWrap;
import com.gylang.netty.sdk.domain.model.AbstractSessionGroup;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.handler.NettyController;
import com.gylang.netty.sdk.repo.DefaultGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gylang
 * data 2020/11/17
 */
@NettyHandler("login")
@Component
public class TouristLoginHandler implements NettyController<String> {

    @Autowired
    private IMContext context;

    private static final KeyLock<String> keyLock = new KeyLock<>();

    @Override
    public void process(IMSession me, String requestBody) {

        MessageProvider messageProvider = context.messageProvider();
        DefaultGroupRepository groupRepository = context.groupRepository();
        me.setAccount(requestBody);
        AbstractSessionGroup defaultGroup = getAndCreateGroup(me, groupRepository);
        boolean join = defaultGroup.join(me);
        if (join) {
            MessageWrap messageWrap = new MessageWrap();
            messageWrap.setSender(me.getAccount());
            messageWrap.setContent(requestBody + "加入群聊组");
            messageProvider.sendGroup(me, "default", messageWrap);

        }
    }

    private AbstractSessionGroup getAndCreateGroup(IMSession me, DefaultGroupRepository groupRepository) {


        AbstractSessionGroup defaultGroup;
        defaultGroup = groupRepository.findByKey("default");
        if (null != defaultGroup) {
            return defaultGroup;
        }
        String key = "im:group_session:" + "default";
        try {
            keyLock.lock(key);
            // 前面的线程可以能已经创建完聊天组, 所以需要再次判断
            defaultGroup = groupRepository.findByKey("default");
            if (null == defaultGroup) {
                defaultGroup = new AbstractSessionGroup("default", me.getAccount(), 1000);
                groupRepository.add("default", defaultGroup);
            }
        } finally {
            keyLock.unlock(key);
        }
        return defaultGroup;
    }
}
