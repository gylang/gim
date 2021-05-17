package com.gylang.gim.api.dto.response;

import com.gylang.gim.api.dto.PtUserDTO;
import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/5/16
 */
@Data
public class WBListUserInfoDTO {


    /** 用户id */
    private String uid;

    /** 黑白名单配置 */
    private String type;

    /** 黑名单 */
    private List<PtUserDTO> black;

    /** 白名单 */
    private List<PtUserDTO> white;

}
