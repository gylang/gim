package com.gylang.im.web.dto;

import com.gylang.im.common.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * (Role)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends BaseDTO {
    private static final long serialVersionUID = -36304856830009628L;


    
    /**
     * 用户id
     */    
    
    private Long uid;

    
    /**
     * 角色类型
     */    
    
    private String roleType;

    
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
    
    private Long createBy;

    
    /**
     * 修改人
     */    
    
    private Long modifyBy;

    
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