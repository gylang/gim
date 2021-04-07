package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.common.mybatis.UserHelper;
import com.gylang.gim.web.entity.HistoryGroupChat;
import com.gylang.gim.web.entity.HistoryPrivateChat;
import com.gylang.gim.web.service.HistoryGroupChatService;
import com.gylang.gim.web.service.HistoryMessageService;
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
    public CommonResult<PageResponse<HistoryPrivateChat>> privateHistory(@RequestBody Page<HistoryPrivateChat> privateChatPage) {

        Long uid = userHelper.getUid();
        return CommonResult.ok(historyMessageService.privateHistory(privateChatPage, String.valueOf(uid)));
    }

    @RequestMapping("/group")
    public CommonResult<PageResponse<HistoryGroupChat>> groupHistory(@RequestBody Page<HistoryGroupChat> groupChatPage) {

        Long uid = userHelper.getUid();
        PageResponse<HistoryGroupChat> page = historyMessageService.groupHistory(groupChatPage, String.valueOf(uid));
        return CommonResult.ok(page);
    }
}
