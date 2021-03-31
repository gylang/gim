package com.gylang.im.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gylang.im.common.mybatis.InnerBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 私聊信息列表(HistoryPrivateChat)实体类
 *
 * @author makejava
 * @since 2021-03-07 11:46:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("history_private_chat")
public class HistoryPrivateChat extends InnerBaseDO {
    private static final long serialVersionUID = -94021602747304846L;


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