package com.gylang.im.api.dto;

import com.gylang.im.api.domain.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 群组信息卡片(ImGroupCard)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImGroupCardDTO extends BaseDTO {
    private static final long serialVersionUID = -68379306861072010L;


    
    /**
     * 群聊名
     */    
    
    private String name;

    
    /**
     * 群主
     */    
    
    private Long groupMaster;

    
    /**
     * 群类型
     */    
    
    private String type;

    
    /**
     * 群标签
     */    
    
    private String label;

    
    /**
     * 群公告
     */    
    
    private String notice;

    
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