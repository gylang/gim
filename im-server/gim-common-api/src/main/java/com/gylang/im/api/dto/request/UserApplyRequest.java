package com.gylang.im.api.dto.request;

import com.gylang.im.api.domain.common.BaseDTO;
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

public class UserApplyRequest extends BaseDTO {


    /**
     * 申请id
     */

    private String applyId;

    /**
     * 回应着id
     */

    private String answerId;

    /**
     * 留言
     */

    private String leaveWord;

    /**
     * 回应操作
     */

    private Integer answerType;



}