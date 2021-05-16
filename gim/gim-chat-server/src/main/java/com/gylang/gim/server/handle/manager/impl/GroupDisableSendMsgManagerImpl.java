package com.gylang.gim.server.handle.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.cmd.ManagerCmd;
import com.gylang.gim.api.constant.mamager.GroupConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.manager.WhiteBlackList;
import com.gylang.gim.api.domain.message.reply.ReplyMessage;
import com.gylang.gim.server.handle.manager.ManagerService;
import com.gylang.gim.server.service.SendAccessService;
import com.gylang.netty.sdk.api.domain.model.GIMSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author gylang
 * data 2021/5/15
 */
@Service
public class GroupDisableSendMsgManagerImpl implements ManagerService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SendAccessService sendAccessService;
    @Override
    public MessageWrap doInvoke(GIMSession session, MessageWrap messageWrap) {

        WhiteBlackList whiteBlackList = JSON.parseObject(messageWrap.getContent(), WhiteBlackList.class);


        ReplyMessage success = ReplyMessage.success(messageWrap);
        if (GroupConstant.QUERY.equals(messageWrap.getCode())) {
            WhiteBlackList result = sendAccessService.queryGroupInfo(whiteBlackList);
            success.setContent(JSON.toJSONString(result));
            return success;
        }
        sendAccessService.updateGroup(whiteBlackList);

        return success;
    }

    @Override
    public String managerType() {
        return ManagerCmd.GROUP_DISABLE_SEND_MSG_MANAGER;
    }
}
