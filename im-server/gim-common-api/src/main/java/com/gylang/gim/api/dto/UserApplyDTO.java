package com.gylang.gim.api.dto;

import lombok.Data;

/**
 * author gylang
 * data 2021/4/14
 */
@Data
public class UserApplyDTO {

    /**
     * 申请id
     */
    
    private String applyId;

    private String username;

    private String nickname;

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
