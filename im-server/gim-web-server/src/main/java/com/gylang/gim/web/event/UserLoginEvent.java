package com.gylang.gim.web.event;

import com.gylang.gim.admin.event.MessageEventListener;
import com.gylang.gim.api.constant.EventTypeConst;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author gylang
 * data 2021/4/16
 */
@Component
public class UserLoginEvent implements MessageEventListener<MessageWrap> {


    @Override
    public void onEvent(String key, MessageWrap m) {

        // todo 用户上线 准备好消息记录 怎么发送离线消息
    }

    @Override
    public List<String> bind() {
        return Collections.singletonList(ChatTypeEnum.NOTIFY.getType() + "_" + EventTypeConst.USER_ONLINE);
    }
}
