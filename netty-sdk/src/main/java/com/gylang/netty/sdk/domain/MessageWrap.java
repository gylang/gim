package com.gylang.netty.sdk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一交互信息包装类
 *
 * @author gylang
 * data 2020/11/3
 * @version v0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageWrap {

    private String key;

    private String sender;

    private String type;

    private String content;

    private String code;

    private String receive;

    private String receiverType;

    private String msgId;
}
