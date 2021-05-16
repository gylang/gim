package com.gylang.grtc.domain;

import com.gylang.netty.sdk.api.domain.model.GIMSession;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间设置
 *
 * @author gylang
 * data 2021/4/23
 */
@Data
public class Room {

    /** 房间名 */
    private String roomName;
    /** 房间id */
    private String roomId;
    /** 入房林牌 */
    private String token;
    /** 创建人 */
    private String createBy;
    /** 房间人数 */
    private Integer num;
    /** 是否1对多 -> 主播模式 */
    private boolean one2Many;
    /** 成员 */
    private Map<String, GIMSession> member = new ConcurrentHashMap<>();
}
