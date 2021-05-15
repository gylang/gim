package com.gylang.gim.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.gim.api.constant.AnswerType;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.constant.cmd.PushChatCmd;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.MessageWrap;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private UserApplyService userApplyService;
    @Resource
    private SocketManager socketManager;

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
        userFriendService.saveBatch(CollUtil.newArrayList(imUserFriend1, imUserFriend2));
        BizFriendService proxy = getProxy();
        // 可以直接通过 操作hash 新增field
        proxy.updateCacheList(friend.getFriendId());
        proxy.updateCacheList(friend.getUid());

        // 更新好友白名单记录
        redisTemplate.opsForSet().add(CacheConstant.WHITE_LIST_CHECK + friend.getUid(), friend.getFriendId());
        redisTemplate.opsForSet().add(CacheConstant.WHITE_LIST_CHECK + friend.getFriendId(), friend.getUid());
        return CommonResult.ok();
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
        // 更新白名单
        redisTemplate.opsForSet().remove(CacheConstant.WHITE_LIST_CHECK + friend.getUid(), friend.getFriendId());
        redisTemplate.opsForSet().remove(CacheConstant.WHITE_LIST_CHECK + friend.getFriendId(), friend.getUid());
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
            //
            PushMessage message = new PushMessage();
            message.setContent(JSON.toJSONString(userApply));

            message.setReceiveId(CollUtil.newArrayList(userApply.getApplyId()));
            MessageWrap messageWrap = MessageWrap.builder()
                    .cmd(PushChatCmd.P2P_PUSH)
                    .type(ChatType.PUSH_CHAT)
                    .sender(userApply.getApplyId())
                    .content(JSON.toJSONString(message))
                    .offlineMsgEvent(false)
                    .qos(1)
                    .build();
            // 发送消息
            socketManager.send(messageWrap);
            // 通知消息
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

            // 给申请者发送hello
            PushMessage message = new PushMessage();
            message.setSender(userApply.getApplyId());
            message.setContent("你好，我们已经是好友拉~");
            message.setReceiveId(CollUtil.newArrayList(userApply.getApplyId()));
            MessageWrap applyMsg = MessageWrap.builder()
                    .qos(1)
                    .cmd(PushChatCmd.P2P_PUSH)
                    .sender(apply.getAnswerId())
                    .receive(apply.getApplyId())
                    .offlineMsgEvent(true)
                    .content(JSON.toJSONString(message))
                    .type(ChatType.PRIVATE_CHAT)
                    .build();
            socketManager.send(applyMsg);

            // 给被申请方发送消息
            MessageWrap answerMsg = applyMsg.copyBasic();
            answerMsg.setSender(apply.getApplyId());
            answerMsg.setReceive(apply.getAnswerId());
            socketManager.send(applyMsg);
            // 更新好友白名单记录
            redisTemplate.opsForSet().add(CacheConstant.WHITE_LIST_CHECK + userApply.getApplyId(), userApply.getAnswerId());
            redisTemplate.opsForSet().add(CacheConstant.WHITE_LIST_CHECK + userApply.getAnswerId(), userApply.getApplyId());
        } else {
            // 拒绝
            MessageWrap rejectMsg = MessageWrap.builder()
                    .qos(1)
                    .sender(apply.getAnswerId())
                    .receive(apply.getApplyId())
                    .content(apply.getAnswerId() + ": 拒绝添加好友!")
                    .type(ChatType.NOTIFY_CHAT)
                    .cmd(SystemChatCmd.NOTIFY)
                    .build();
            socketManager.send(rejectMsg);
        }

        return CommonResult.ok();
    }
}
