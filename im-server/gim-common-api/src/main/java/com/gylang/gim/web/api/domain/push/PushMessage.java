package com.gylang.gim.web.api.domain.push;

import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/4/7
 */
@Data
public class PushMessage {

    /** 接收者id列表 */
    private List<String> receiveId;
    /** 内容 */
    private String content;
}
