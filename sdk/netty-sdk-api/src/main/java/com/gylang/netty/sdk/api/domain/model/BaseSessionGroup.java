package com.gylang.netty.sdk.api.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;

/**
 * 负责存储,发送和通知
 *
 * @author gylang
 * data 2020/10/29
 * @version v0.0.1
 */
@Setter
@Getter
public class BaseSessionGroup {

    /**
     * 组名
     */
    private String name;
    /**
     * 创建者
     */
    private String creator;

    /** 群主 */
    private String master;
    /**
     * 组关键字
     */
    private String key;

    /** 群组id */
    private String groupId;
    /**
     * 房间密码
     */
    private String password;

    /**
     * 队列
     */
    private BlockingQueue<GIMSession> memberList;

}
