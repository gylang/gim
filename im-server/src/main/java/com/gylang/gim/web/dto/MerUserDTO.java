package com.gylang.gim.web.dto;
import com.gylang.gim.common.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;


/**
 * (MerUser)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerUserDTO extends BaseDTO {
    private static final long serialVersionUID = 723354004020202662L;


    
    /**
     * 用户名
     */    
    
    private String username;

    
    /**
     * 密码
     */    
    
    private String password;

    
    /**
     * 电话号码
     */    
    
    private String tel;

    
    /**
     * 昵称
     */    
    
    private String nickname;

    
    /**
     * 密码盐
     */    
    
    private String salt;

    
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