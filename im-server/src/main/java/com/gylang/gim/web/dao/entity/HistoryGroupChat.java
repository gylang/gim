package com.gylang.gim.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.gim.web.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 群主信息列表(HistoryGroupChat)实体类
 *
 * @author makejava
 * @since 2021-03-07 11:46:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("history_group_chat")
public class HistoryGroupChat extends InnerBaseDO {
    private static final long serialVersionUID = -65015129491794899L;


    /**
     * 消息id
     */
    @TableField("msg_id")
    private String msgId;

    /**
     * 回应着id
     */
    @TableField("time_stamp")
    private Long timeStamp;

    /**
     * 发送者id
     */
    @TableField("send_id")
    private String sendId;

    /**
     * 接收者id
     */
    @TableField("receive")
    private String receive;

    /**
     * 消息体
     */
    @TableField("message")
    private String message;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;


}