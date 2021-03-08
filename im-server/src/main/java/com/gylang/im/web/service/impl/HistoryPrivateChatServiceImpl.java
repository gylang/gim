package com.gylang.im.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.dao.entity.HistoryPrivateChat;
import com.gylang.im.dao.mapper.HistoryPrivateChatMapper;
import com.gylang.im.web.service.HistoryPrivateChatService;
import org.springframework.stereotype.Service;


/**
 * 私聊信息列表(HistoryPrivateChat)表服务实现类
 *
 * @author makejava
 * @since 2021-03-07 11:46:49
 */
@Service("historyPrivateChatService")
public class HistoryPrivateChatServiceImpl extends ServiceImpl<HistoryPrivateChatMapper, HistoryPrivateChat> implements HistoryPrivateChatService {

   
}