package com.gylang.grtc.context;

/**
 * @author gylang
 * data 2021/4/23
 */
public interface RoomMatch {
    /**
     * 查找
     *
     * @param sort  排序
     * @param index 下表
     * @param key   key
     * @param room  房间
     * @return 匹配 true
     */
    boolean match();


}
