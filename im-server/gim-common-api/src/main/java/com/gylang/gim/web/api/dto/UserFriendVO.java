package com.gylang.gim.web.api.dto;

import com.gylang.gim.web.api.domain.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gylang
 * data 2021/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFriendVO extends BaseDTO {

    private Long id;

    /**
     * 用户id
     */

    private Long uid;

    /**
     * 好友id
     */

    private Long friendId;

    /**
     * 好友分组id
     */

    private Long groupId;

    /**
     * 好友备注, 默认为添加好友时的名称
     */

    private String remarkName;

    /**
     * 普通好友0 星级好友1 特别关注2
     */

    private Integer starFlag;

    /**
     * 用户名
     */

    private String username;


    /**
     * 邮箱
     */

    private String email;

    /**
     * 电话号码
     */

    private String tel;

    /**
     * 昵称
     */

    private String nickname;

    /**
     * 用户名称
     */

    private String name;

    /**
     * 头像
     */

    private String avatar;

    /**
     * 简介
     */

    private String intro;


}
