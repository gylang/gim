package com.gylang.gim.im.constant;

/**
 * @author gylang
 * data 2021/3/27
 */
public enum LuaScriptConstant {
    ;

    public static final String CHECK_WHITE_AND_BLACK =
            "local senderId = KEYS[1]\n" +
                    "local receiveId = KEYS[2]\n" +
                    "local config = redis.call('get','IM:USER:CONFIG:' + receiveId)\n" +
                    "if(null == config)\n" +
                    "then\n" +
                    "    return 1\n" +
                    "else\n" +
                    "    if(0 == config)\n" +
                    "    then\n" +
                    "        return not(redis.call('SISMEMBER','IM:USER:CONFIG:WLC:' + receiveId, senderId))\n" +
                    "    else\n" +
                    "        return redis.call('SISMEMBER','IM:USER:CONFIG:BLC:' + receiveId, senderId)\n" +
                    "end";
}
