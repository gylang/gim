package com.gylang.netty.sdk.domain;

import lombok.Data;

/**
 * @author gylang
 * data 2020/12/8
 */
@Data
public class GylHeader {

    /** 协议类型 */
    private short protocolType;

    private byte version;
    /** 魔数 */
    private byte magic;
    /** 序列化方式 */
    private byte serialize;
    /** 服务类型 */
    private short serverType;
    /** 长度 */
    private int length;
}
