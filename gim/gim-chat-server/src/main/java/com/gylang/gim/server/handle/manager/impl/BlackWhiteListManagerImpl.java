package com.gylang.gim.server.handle.manager.impl;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.GroupConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.manager.WhiteBlackList;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.gim.server.service.SendAccessService;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/5/15
 */
@Service
public class BlackWhiteListManagerImpl implements ManagerService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private SendAccessService sendAccessService;

    @Override
    public MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap) {

        WhiteBlackList whiteBlackList = JSON.parseObject(messageWrap.getContent(), WhiteBlackList.class);

        ReplyMessage success = ReplyMessage.success(messageWrap);
        if (GroupConstant.QUERY.equals(messageWrap.getCode())) {
            WhiteBlackList result = sendAccessService.queryPrivateInfo(whiteBlackList);
            success.setContent(JSON.toJSONString(result));
            return success;
        }

        sendAccessService.updatePrivate(whiteBlackList);

        return success;
    }

    @Override
    public String managerType() {
        return ManagerCmd.BLACK_WHITE_LIST_MANAGER;
    }
}
