package com.gylang.gim.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.cache.CacheManager;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.dto.UserFriendVO;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.web.common.constant.AnswerType;
import com.gylang.gim.web.common.constant.CacheConstant;
import com.gylang.gim.web.common.util.Asserts;
import com.gylang.gim.web.entity.ImUserFriend;
import com.gylang.gim.web.entity.UserApply;
import com.gylang.gim.web.service.ImUserFriendService;
import com.gylang.gim.web.service.UserApplyService;
import com.gylang.gim.web.service.biz.BizFriendService;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.provider.MessageProvider;
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
    private CacheManager cacheManager;
    @Resource
    private UserApplyService userApplyService;

    private MessageProvider messageProvider;

    @Override
    @Cacheable(value = CacheConstant.USER_FRIEND_LIST_PREFIX, key = "#p0")
    public List<UserFriendVO> getFriendList(Long uid) {

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
        return CommonResult.ok();
    }

    @Override
    public BizFriendService getProxy() {
        return (BizFriendService) AopContext.currentProxy();
    }

    @CacheEvict(value = CacheConstant.USER_FRIEND_LIST_PREFIX, key = "#p0")
    @Override
    public List<UserFriendVO> updateCacheList(Long id) {
        List<UserFriendVO> userFriendList =
                userFriendService.selectFriendListByUid(id);
        if (CollUtil.isNotEmpty(userFriendList)) {
            return userFriendList;
        }
        return CollUtil.empty(ImUserFriendDTO.class);
    }

    @Override
    public CommonResult<Boolean> del(ImUserFriendDTO friend) {

        userFriendService.removeByIds(CollUtil.newArrayList(friend.getFriendId(), friend.getUid()));
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

            // 添加申请, 并通知好友
            userApplyService.save(userApply);
            MessageWrap messageWrap = MessageWrap.builder()
                    .cmd("111")
                    .sender(userApply.getApplyId())
                    .content(JSON.toJSONString(userApply))
                    .offlineMsgEvent(false)
                    .qos(1)
                    .build();
            // 发送消息
            IMSession imSession = new IMSession();
            imSession.setAccount(userApply.getApplyId());
            messageProvider.sendMsg(imSession, userApply.getAnswerId(), messageWrap);
            // 通知消息
        }
        return CommonResult.ok();
    }

    @Override
    public CommonResult<Boolean> answer(UserApply userApply) {

        UserApply apply = userApplyService.getById(userApply.getAnswerId());
        Asserts.notNull(apply, "好友申请不存在");

        IMSession applySession = new IMSession();
        applySession.setAccount(apply.getAnswerId());
        if (AnswerType.AGREEMENT == userApply.getAnswerType()) {
            // 同意添加好友
            apply.setAnswerType(userApply.getAnswerType());

            // 给申请者发送hello
            MessageWrap applyMsg = MessageWrap.builder()
                    .qos(1)
                    .cmd("111")
                    .sender(apply.getAnswerId())
                    .receive(apply.getApplyId())
                    .offlineMsgEvent(true)
                    .content("你好，我们已经是好友拉~")
                    .type(ChatTypeEnum.PRIVATE_CHAT.getType())
                    .build();
            messageProvider.sendMsg(applySession, apply.getApplyId(), applyMsg);

            // 给被申请方发送消息
            MessageWrap answerMsg = applyMsg.copyBasic();
            answerMsg.setSender(apply.getApplyId());
            answerMsg.setReceive(apply.getAnswerId());
            IMSession answerSession = new IMSession();
            answerSession.setAccount(apply.getApplyId());
            messageProvider.sendMsg(answerSession, apply.getAnswerId(), answerMsg);
        } else {
            // 拒绝
            MessageWrap rejectMsg = MessageWrap.builder()
                    .qos(1)
                    .sender(apply.getAnswerId())
                    .receive(apply.getApplyId())
                    .content(apply.getAnswerId() + ": 拒绝添加好友!")
                    .type(ChatTypeEnum.NOTIFY.getType())
                    .cmd(SystemChatCmd.NOTIFY)
                    .build();
            messageProvider.sendMsg(applySession, apply.getAnswerId(), rejectMsg);
        }

        return CommonResult.ok();
    }
}