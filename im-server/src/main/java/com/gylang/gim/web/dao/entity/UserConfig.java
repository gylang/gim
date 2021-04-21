package com.gylang.gim.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户设置表(UserConfig)实体类
 *
 * @author makejava
 * @since 2021-03-06 17:10:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_config")
public class UserConfig extends InnerBaseDO {
    private static final long serialVersionUID = -96919080579193597L;

            
    /**
     * 用户id
     */    @TableField("uid") 
    private Long uid;
               
    /**
     * 是否接收允许陌生人信息
     */    @TableField("receive_stranger") 
    private Integer receiveStranger;
               
    /**
     * 是否允许添加好友
     */    @TableField("receive_add_friend") 
    private Integer receiveAddFriend;
                                       
    /**
     * 租户id
     */    @TableField("tenant_id") 
    private Long tenantId;
   



}