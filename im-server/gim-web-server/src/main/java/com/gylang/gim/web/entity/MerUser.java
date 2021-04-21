package com.gylang.gim.web.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * (MerUser)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("mer_user")
public class MerUser extends InnerBaseDO {
    private static final long serialVersionUID = 471544794368274756L;

            
    /**
     * 用户名
     */    @TableField("username") 
    private String username;
               
    /**
     * 密码
     */    @TableField("password") 
    private String password;
               
    /**
     * 电话号码
     */    @TableField("tel") 
    private String tel;
               
    /**
     * 昵称
     */    @TableField("nickname") 
    private String nickname;
               
    /**
     * 密码盐
     */    @TableField("salt") 
    private String salt;
                                       
    /**
     * 租户id
     */    @TableField("tenant_id") 
    private Long tenantId;
   



}