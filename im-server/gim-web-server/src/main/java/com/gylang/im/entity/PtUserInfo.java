package com.gylang.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.im.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户信息表(PtUserInfo)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pt_user_info")
public class PtUserInfo extends InnerBaseDO {
    private static final long serialVersionUID = 994256037298868216L;


    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 简介
     */
    @TableField("intro")
    private String intro;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;


}