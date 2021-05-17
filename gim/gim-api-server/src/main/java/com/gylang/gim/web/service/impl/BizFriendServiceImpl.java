package com.gylang.gim.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.gim.api.constant.AnswerType;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.cmd.NotifyChatCmd;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.domain.manager.BlackWhiteList;
import com.gylang.gim.api.domain.push.PushMessage;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.dto.UserFriendVO;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.web.common.util.Asserts;
import com.gylang.gim.web.entity.ImUserFriend;
import com.gylang.gim.web.entity.UserApply;
import com.gylang.gim.web.service.ImUserFriendService;
import com.gylang.gim.web.service.UserApplyService;
import com.gylang.gim.web.service.biz.BizFriendService;
import com.gylang.gim.web.service.im.ImUserBlackWhiteManager;
import com.gylang.gim.web.service.im.ImUserPushManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gylang
 * data 2021/3/6
 */
@Service
@Slf4j
public class BizFriendServiceImpl implements BizFriendService {

    @Resource
    private ImUserFriendService userFriendService;
    @Resource
    private ImUserPushManager imUserPushManager;
    @Resource
    private UserApplyService userApplyService;
    @Resource
    private SocketManager socketManager;
    @Resource
    private ImUserBlackWhiteManager whiteBlackManager;

    @Override
    @Cacheable(value = CacheConstant.USER_FRIEND_LIST_PREFIX, key = "#p0")
    public List<UserFriendVO> getFriendList(String uid) {

        return updateCacheList(uid);
    }

    @Override
    public CommonResult<Boolean> addFriend(ImUserFriendDTO friend) {

        // 新增两条用户记录
        ImUserFriend imUserFriend1 = new ImUserFriend();
        imUserFriend1.setUid(friend.getUid());
        imUserFriend1.setFriendId(friend.getFriendId());
        ImUserFriend imUserFriend2 = new ImUserFriend();
        imUserFriend2.setUid(friend.getFriendId());
        imUserFriend2.setFriendId(friend.getUid());
        changeBlackWhiteList(friend);
        userFriendService.saveBatch(CollUtil.newArrayList(imUserFriend1, imUserFriend2));
        BizFriendService proxy = getProxy();
        // 可以直接通过 操作hash 新增field
        proxy.updateCacheList(friend.getFriendId());
        proxy.updateCacheList(friend.getUid());

        // 更新好友白名单记录
        return CommonResult.ok();
    }

    private void changeBlackWhiteList(ImUserFriendDTO friend) {
        BlackWhiteList blackWhiteList = new BlackWhiteList();
        blackWhiteList.setId(friend.getFriendId());
        blackWhiteList.setAddBlack(CollUtil.newArrayList(friend.getUid()));
        whiteBlackManager.save(blackWhiteList);
        blackWhiteList.setId(friend.getUid());
        blackWhiteList.setAddWhite(CollUtil.newArrayList(friend.getFriendId()));
        whiteBlackManager.save(blackWhiteList);
    }

    @Override
    public BizFriendService getProxy() {
        return (BizFriendService) AopContext.currentProxy();
    }

    @CacheEvict(value = CacheConstant.USER_FRIEND_LIST_PREFIX, key = "#p0")
    @Override
    public List<UserFriendVO> updateCacheList(String id) {
        List<UserFriendVO> userFriendList =
                userFriendService.selectFriendListByUid(id);
        if (CollUtil.isNotEmpty(userFriendList)) {
            return userFriendList;
        }

        return CollUtil.empty(ImUserFriendDTO.class);
    }

    @Override
    public CommonResult<Boolean> del(ImUserFriendDTO friend) {

        userFriendService.lambdaUpdate()
                .or(w -> w.eq(ImUserFriend::getUid, friend.getUid()).eq(ImUserFriend::getFriendId, friend.getFriendId()))
                .or(w -> w.eq(ImUserFriend::getFriendId, friend.getUid()).eq(ImUserFriend::getUid, friend.getFriendId()))
                .remove();

        getProxy().updateCacheList(friend.getFriendId());
        return CommonResult.ok();
    }

    @Override
    public CommonResult<Boolean> applyFriend(UserApply userApply) {

        // 判断是否为好友
        ImUserFriend userFriend = userFriendService.getOne(new QueryWrapper<ImUserFriend>()
                .eq("uid", userApply.getApplyId())
                .eq("friend_id", userApply.getAnswerId()));
        if (null == userFriend) {
            userApply.setAnswerType(AnswerType.NOT_PROCESS);
            // 添加申请, 并通知好友
            userApplyService.save(userApply);

            // 通知消息
            PushMessage message = new PushMessage();
            message.setContent(JSON.toJSONString(userApply));
            message.setSender(userApply.getApplyId());
            message.setType(ChatType.NOTIFY_CHAT);
            message.setCmd(NotifyChatCmd.USER_APPLY_FRIEND);
            message.setContent("用户申请添加好友");
            message.setQos(1);
            message.setReceive(userApply.getAnswerId());
            imUserPushManager.push(1, message);
        }
        return CommonResult.ok();
    }

    @Override
    public CommonResult<Boolean> answer(UserApply userApply) {

        UserApply apply = userApplyService.getById(userApply.getId());
        Asserts.notNull(apply, "好友申请不存在");
        Asserts.isTrue(userApply.getAnswerId().equals(apply.getAnswerId()), "好友申请不存在");

        if (AnswerType.AGREEMENT == userApply.getAnswerType()) {
            // 同意添加好友
            apply.setAnswerType(userApply.getAnswerType());
            userApplyService.save(apply);

            // 新增好友关系
            ImUserFriendDTO friend = new ImUserFriendDTO();
            friend.setUid(userApply.getApplyId());
            friend.setFriendId(userApply.getAnswerId());
            addFriend(friend);

            // 给申请者 应答者 发送hello
            PushMessage message = new PushMessage();
            message.setSender(userApply.getApplyId());
            message.setContent("你好，我们已经是好友拉~");
            message.setReceiveId(CollUtil.newArrayList(userApply.getAnswerId()));
            message.setType(ChatType.PRIVATE_CHAT);
            message.setQos(2);
            imUserPushManager.push(1, message);
            message.setSender(userApply.getAnswerId());
            message.setReceiveId(CollUtil.newArrayList(userApply.getApplyId()));
            imUserPushManager.push(1, message);

        } else {

            // 给申请者 发送拒绝通知
            PushMessage message = new PushMessage();
            message.setSender(userApply.getApplyId());
            message.setContent("拒绝添加好友");
            message.setReceiveId(CollUtil.newArrayList(userApply.getAnswerId()));
            message.setType(ChatType.NOTIFY_CHAT);
            message.setCmd(NotifyChatCmd.USER_APPLY_FRIEND_ANSWER);
            message.setCode(CommonConstant.FALSE_INT_STR);
            message.setQos(2);
            imUserPushManager.push(1, message);
        }

        return CommonResult.ok();
    }
}
