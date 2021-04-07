package com.gylang.gim.web.api.domain;

import lombok.Data;

/**
 * @author gylang
 * data 2021/4/6
 */
@Data
public class UserLinkStatus {

    private String accountId;

    private String ip;

    private String channel;

    /** 0 已下线 1在线  */
    private String status;
}
