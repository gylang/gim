package com.gylang.im.dao.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.gylang.im.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;


/**
 * 群组信息卡片(ImGroupCard)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("im_group_card")
public class ImGroupCard extends InnerBaseDO {
    private static final long serialVersionUID = -80970420113882219L;

            
    /**
     * 群聊名
     */    @TableField("name") 
    private String name;
               
    /**
     * 群主
     */    @TableField("group_master") 
    private Long groupMaster;
               
    /**
     * 群类型
     */    @TableField("type") 
    private String type;
               
    /**
     * 群标签
     */    @TableField("label") 
    private String label;
               
    /**
     * 群公告
     */    @TableField("notice") 
    private String notice;
                                       
    /**
     * 租户id
     */    @TableField("tenant_id") 
    private Long tenantId;
   



}