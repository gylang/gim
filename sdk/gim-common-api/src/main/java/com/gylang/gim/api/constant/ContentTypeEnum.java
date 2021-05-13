package com.gylang.gim.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gylang
 * data 2021/3/26
 */
@Getter
@AllArgsConstructor
public enum ContentTypeEnum {
    /**
     * 各类消息类型
     */
    TEXT(1, "内容消息类型"),
    VOICE(2, "语言消息"),
    IMAGINE(3, "图片消息"),
    GIF(4, "GIF动态图片"),
    IMG_TEXT(5, "图文"),
    FILE(6, "文件"),
    LOCAL(7, "位置"),
    VIDEO(8, "视频"),
    REFERENCE(9, "引用"),
    COMBINE(10, "合并转发"),

    ;
    private final int type;

    private final String mark;
}
