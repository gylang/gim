package com.gylang.gim.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 好友关系表(ImUserFriend)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("im_user_friend")
public class ImUserFriend extends InnerBaseDO {
    private static final long serialVersionUID = -87369595066076512L;


    /**
     * 用户id
     */
    @TableField("uid")
    private String uid;

    /**
     * 好友id
     */
    @TableField("friend_id")
    private String friendId;

    /**
     * 好友分组id
     */
    @TableField("group_id")
    private String groupId;

    /**
     * 好友备注, 默认为添加好友时的名称
     */
    @TableField("remark_name")
    private String remarkName;

    /**
     * 普通好友0 星级好友1 特别关注2
     */
    @TableField("star_flag")
    private Integer starFlag;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;


}