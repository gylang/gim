package com.gylang.gim.web.dto;

import com.gylang.gim.common.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 用户信息表(PtUserInfo)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PtUserInfoDTO extends BaseDTO {
    private static final long serialVersionUID = -95700292471008662L;


    
    /**
     * 用户id
     */    
    
    private Long uid;

    
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