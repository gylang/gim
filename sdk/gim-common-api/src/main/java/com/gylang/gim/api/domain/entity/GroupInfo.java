package com.gylang.gim.api.domain.entity;

import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/5/16
 */
@Data
public class GroupInfo {

    /**
     * 组名
     */
    private String name;
    /**
     * 创建者
     */
    private String creator;

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

    /** ids */
    private List<String> ids;
}
