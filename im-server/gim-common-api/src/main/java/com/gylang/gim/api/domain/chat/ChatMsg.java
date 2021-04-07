package com.gylang.gim.api.domain.chat;

import lombok.Data;

/**
 * @author gylang
 * data 2021/4/2
 */
@Data
public class ChatMsg {

    /** 昵称 */
    private String nickname;
    /** 内容 */
    private String content;
    /** 头像 */
    private String avatar;
    /** 时间戳 */
    private Long timeStamp;
    /** 消息id */
    private String msgId;

    private boolean me;

}
