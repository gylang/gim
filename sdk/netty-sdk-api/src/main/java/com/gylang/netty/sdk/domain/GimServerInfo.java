package com.gylang.netty.sdk.domain;

import lombok.Data;

/**
 * @author gylang
 * data 2021/5/15
 */
@Data
public class GimServerInfo {

    /** ip */
    private String ip;

    /** 端口号 */
    private Integer port;

    /** 标识关键字 */
    private String key;
}
