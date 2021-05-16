package com.gylang.gim.api.dto.response;

import lombok.Data;

/**
 * @author gylang
 * data 2021/5/16
 */
@Data
public class UserGroupInfoDTO {

    private Long id;

    private String uid;

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
     * 状态
     */
    private Integer status;


    /**
     * 0未删除 1已删除
     */
    private Integer isDelete;


    /**
     * 租户id
     */
    private Long tenantId;

}
