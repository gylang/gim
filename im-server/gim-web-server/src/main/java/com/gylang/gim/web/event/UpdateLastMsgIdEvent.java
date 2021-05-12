package com.gylang.gim.web.event;

import cn.hutool.core.collection.CollUtil;
import com.gylang.gim.admin.event.MessageEventListener;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gylang
 * data 2021/5/12
 */
@Component
public class UpdateLastMsgIdEvent implements MessageEventListener<MessageWrap> {

    @Override
    public void onEvent(String key, MessageWrap m) {

    }

    @Override
    public List<String> bind() {
        return CollUtil.newArrayList(ChatTypeEnum.PRIVATE_CHAT_LAST_MSG_ID + "-" + null);
    }
}
