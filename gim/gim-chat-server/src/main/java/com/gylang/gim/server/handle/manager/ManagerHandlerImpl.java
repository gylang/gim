package com.gylang.gim.server.handle.manager;

import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.api.enums.BaseResultCode;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.netty.sdk.api.annotation.NettyHandler;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import com.gylang.netty.sdk.api.handler.IMRequestHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gylang
 * data 2021/5/6
 */
@NettyHandler(ChatType.MANAGER)
@Component
public class ManagerHandlerImpl implements IMRequestHandler, InitializingBean {

    @Resource
    private Map<String, ManagerService> managerServiceMap;

    @Override
    public Object process(GIMSession me, MessageWrap message) {

        ManagerService managerService = managerServiceMap.get(message.getCmd());
        if (null != managerService) {
            MessageWrap messageWrap = managerService.doInvoke(me, message);
            if (null != messageWrap) {
                return ReplyMessage.reply(message, BaseResultCode.OK);
            }
            return null;
        }
        return ReplyMessage.reply(message, BaseResultCode.SYSTEM_VISIT_RESOURCE_ERROR);
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        managerServiceMap = managerServiceMap.values().stream()
                .collect(Collectors.toMap(ManagerService::managerType, ms -> ms));

    }
}
