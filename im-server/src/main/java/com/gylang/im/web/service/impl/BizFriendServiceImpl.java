package com.gylang.im.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.cache.CacheManager;
import com.gylang.im.common.constant.AnswerType;
import com.gylang.im.common.constant.CacheConstant;
import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.common.util.Asserts;
import com.gylang.im.common.util.MappingUtil;
import com.gylang.im.dao.entity.ImUserFriend;
import com.gylang.im.dao.entity.UserApply;
import com.gylang.im.im.constant.BizChatCmd;
import com.gylang.im.web.dto.ImUserFriendDTO;
import com.gylang.im.web.service.ImUserFriendService;
import com.gylang.im.web.service.UserApplyService;
import com.gylang.im.web.service.biz.BizFriendService;
import com.gylang.netty.sdk.constant.ChatTypeEnum;
import com.gylang.netty.sdk.constant.SystemMessageType;
import com.gylang.netty.sdk.domain.MessageWrap;
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
    @Resource
    private MessageProvider messageProvider;

    @Override
    @Cacheable(value = CacheConstant.USER_FRIEND_LIST_PREFIX, key = "#p0")
    public List<ImUserFriendDTO> getFriendList(Long uid) {

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
    public List<ImUserFriendDTO> updateCacheList(Long id) {
        List<ImUserFriend> userFriendList =
                userFriendService.list(new QueryWrapper<ImUserFriend>()
                        .eq("uid", id));
        if (CollUtil.isNotEmpty(userFriendList)) {
            return MappingUtil.mapAsList(userFriendList, ImUserFriendDTO.class);
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
                    .cmd(BizChatCmd.APPLY_FRIEND_NOTIFY)
                    .sender(userApply.getApplyId())
                    .content(JSON.toJSONString(userApply))
                    .offlineMsgEvent(false)
                    .qos(true)
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
                    .qos(true)
                    .cmd(BizChatCmd.PRIVATE_CHAT)
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
                    .qos(true)
                    .sender(apply.getAnswerId())
                    .receive(apply.getApplyId())
                    .content(apply.getAnswerId() + ": 拒绝添加好友!")
                    .type(ChatTypeEnum.NOTIFY.getType())
                    .cmd(SystemMessageType.NOTIFY)
                    .build();
            messageProvider.sendMsg(applySession, apply.getAnswerId(), rejectMsg);
        }

        return CommonResult.ok();
    }
}
