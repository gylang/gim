package com.gylang.im.controller;

import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.domain.common.PageResponse;
import com.gylang.im.common.mybatis.UserHelper;
import com.gylang.im.dao.entity.HistoryGroupChat;
import com.gylang.im.dao.entity.HistoryPrivateChat;
import com.gylang.im.service.HistoryMessageService;
import com.gylang.im.service.HistoryGroupChatService;
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
    private HistoryMessageService historyMessageService;
    @Autowired
    private HistoryGroupChatService historyGroupChatService;

    @RequestMapping("/private")
    public CommonResult<PageResponse<HistoryPrivateChat>> privateHistory(@RequestBody PageResponse<HistoryPrivateChat> privateChatPage) {

        Long uid = userHelper.getUid();
        return CommonResult.ok(historyMessageService.privateHistory(privateChatPage, String.valueOf(uid)));
    }

    @RequestMapping("/group")
    public CommonResult<PageResponse<HistoryGroupChat>> groupHistory(@RequestBody PageResponse<HistoryGroupChat> groupChatPage) {

        Long uid = userHelper.getUid();
        PageResponse<HistoryGroupChat> page = historyMessageService.groupHistory(groupChatPage, String.valueOf(uid));
        return CommonResult.ok(page);
    }
}
