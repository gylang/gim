package com.gylang.gim.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * 好友申请表(UserApply)实体类
 *
 * @author makejava
 * @since 2021-03-06 20:46:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_apply")
public class UserApply extends InnerBaseDO {
    private static final long serialVersionUID = 736817613818893005L;


    /**
     * 申请id
     */
    @TableField("apply_id")
    private String applyId;

    /**
     * 回应着id
     */
    @TableField("answer_id")
    private String answerId;

    /**
     * 留言
     */
    @TableField("leave_word")
    private String leaveWord;

    /**
     * 回应操作
     */
    @TableField("answer_type")
    private Integer answerType;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;


}