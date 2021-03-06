package com.gylang.gim.api.dto;

import com.gylang.gim.api.domain.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 好友关系表(ImUserFriend)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImUserFriendDTO extends BaseDTO {
    private static final long serialVersionUID = 767112916266390539L;


    /**
     * 用户id
     */

    private String uid;


    /**
     * 好友id
     */

    private String friendId;


    /**
     * 好友分组id
     */

    private String groupId;


    /**
     * 好友备注, 默认为添加好友时的名称
     */

    private String nickname;

    private String username;

    private String avatar;

    /**
     * 普通好友0 星级好友1 特别关注2
     */

    private Integer starFlag;


    /**
     * 创建时间
     */

    private Date createTime;


    /**
     * 修改时间
     */

    private Date updateTime;


    /**
     * 创建人
     */

    private String createBy;


    /**
     * 修改人
     */

    private String modifyBy;


    /**
     * 状态 0 未处理 1已处理
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