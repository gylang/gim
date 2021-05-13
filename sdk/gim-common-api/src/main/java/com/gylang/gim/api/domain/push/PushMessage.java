package com.gylang.gim.api.domain.push;

import com.gylang.gim.api.domain.common.MessageWrap;
import lombok.Data;

import java.util.Collection;

/**
 * @author gylang
 * data 2021/4/7
 */
@Data
public class PushMessage extends MessageWrap {

    /** 接收者id列表 */
    private Collection<String> receiveId;


}
