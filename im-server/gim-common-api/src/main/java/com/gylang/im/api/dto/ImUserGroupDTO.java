package com.gylang.im.api.dto;

import com.gylang.im.api.domain.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 群聊好友人员关系(ImUserGroup)实体类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImUserGroupDTO extends BaseDTO {
    private static final long serialVersionUID = -28584608395312191L;


    /**
     * 用户id
     */

    private Long uid;


    /**
     * 好友分组id
     */

    private Long groupId;


    /**
     * 好友备注, 默认为添加好友时的名称
     */

    private String nickname;


    /**
     * 普通好友0 星级好友1 特别关注2
     */

    private Integer starFlag;


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