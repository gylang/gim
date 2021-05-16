package com.gylang.gim.web.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * (Role)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("role")
public class Role extends InnerBaseDO {
    private static final long serialVersionUID = -60865937735142362L;

            
    /**
     * 用户id
     */    @TableField("uid") 
    private String uid;
               
    /**
     * 角色类型
     */    @TableField("role_type") 
    private String roleType;
                                       
    /**
     * 租户id
     */    @TableField("tenant_id") 
    private Long tenantId;
   



}