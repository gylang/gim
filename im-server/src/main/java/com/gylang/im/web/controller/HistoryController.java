package com.gylang.im.web.controller;

import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.common.dto.PageDTO;
import com.gylang.im.common.mybatis.UserHelper;
import com.gylang.im.common.util.Asserts;
import com.gylang.im.dao.entity.HistoryGroupChat;
import com.gylang.im.dao.entity.HistoryPrivateChat;
import com.gylang.im.web.service.HistoryGroupChatService;
import com.gylang.im.web.service.HistoryPrivateChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息主动拉，当前策略是 通过最新保存的时间戳，上一毫秒（保证不会同一时间消息丢失）查询
 *
 * @author gylang
 * data 2021/3/19
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private UserHelper userHelper;
    @Autowired
    private HistoryPrivateChatService historyPrivateChatService;
    @Autowired
    private HistoryGroupChatService historyGroupChatService;

    @RequestMapping("/private")
    public CommonResult<PageDTO<HistoryPrivateChat>> privateHistory(@RequestBody PageDTO<HistoryPrivateChat> privateChatPage) {

        Long uid = userHelper.getUid();
        HistoryPrivateChat param = privateChatPage.getParam();
        Asserts.notNull(param, "最新消息id不能为空");
        Asserts.notNull(param.getMsgId(), "最新消息id不能为空");
        param.setTargetId(uid);
        param.setTimeStamp(param.getTimeStamp() - 1);
        PageDTO<HistoryPrivateChat> page = historyPrivateChatService.page(privateChatPage);

        return CommonResult.ok(page);
    }

    @RequestMapping("/group")
    public CommonResult<PageDTO<HistoryGroupChat>> groupHistory(@RequestBody PageDTO<HistoryGroupChat> groupChatPage) {

        Long uid = userHelper.getUid();
        HistoryGroupChat param = groupChatPage.getParam();
        Asserts.notNull(param, "最新消息id不能为空");
        Asserts.notNull(param.getMsgId(), "最新消息id不能为空");
        groupChatPage.getParam().setTargetId(uid);
        param.setTimeStamp(param.getTimeStamp() - 1);
        PageDTO<HistoryGroupChat> page = historyGroupChatService.page(groupChatPage);
        return CommonResult.ok(page);
    }
}
