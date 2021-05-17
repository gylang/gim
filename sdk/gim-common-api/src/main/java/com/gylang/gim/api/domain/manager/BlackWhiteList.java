package com.gylang.gim.api.domain.manager;

import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/5/15
 */
@Data
public class BlackWhiteList {

    /** 用户id */
    private String id;
    /** 黑白名单配置 */
    private String type;
    /** 新增白名单用户 */
    private List<String> addWhite;
    /** 删除白名单用户 */
    private List<String> removeWhite;
    /** 删除黑名单用户 */
    private List<String> removeBlack;
    /** 新增黑名单用户 */
    private List<String> addBlack;
}
