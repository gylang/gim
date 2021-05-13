package com.gylang.gim.api.dto;

import com.gylang.gim.api.domain.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * (PtUser)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PtUserDTO extends BaseDTO {
    private static final long serialVersionUID = 190771434328204337L;


    
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