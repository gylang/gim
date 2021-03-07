package com.gylang.im.web.dto.msg;

import lombok.Data;

/**
 * @author gylang
 * data 2021/3/5
 */
@Data
public class PrivateChatMsg {

    /** 消息内容 */
    private String content;
    /** 消息类型 */
    private String msgType;

}
