package com.gylang.netty.sdk.util;

import com.gylang.netty.sdk.domain.MessageWrap;

/**
 * 消息id生成策略
 * | 时间戳 | 序列号 | 消息类型 | 会话id |
 * <p>
 * 为何这样实现, 因为会话都是绑定一个服务器的, 因为是socket连接
 * 所以, 用户的自增不会受到其他服务影响.
 *
 * @author gylang
 * data 2021/3/2
 */
public class MsgIdUtil {

    private MsgIdUtil() {
    }

    /** 16^3 -1 = 4905 */
    private static final int MAX_MESSAGE_SEQ = 0xFFF;
    private static final int SEQ_BIT = Integer.bitCount(MAX_MESSAGE_SEQ);
    /** 当前自增序列号 */
    private static int seq = 0;
    /** 当前时间戳 */
    private static long currentTimeStamp = 0L;

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取自增id
     *
     * @param message 消息
     * @return 消息id
     */
    public static String increase(MessageWrap message) {

        long timeStamp = getTimeStamp();
        message.setTimeStamp(timeStamp);
        int offId = offId(timeStamp);
        // 时间戳 + 序列化
        return String.join("-", Long.toHexString(timeStamp), String.valueOf(offId),
                Integer.toHexString(message.getType()), Long.toHexString(null != message.getReceive() ? message.getReceive() : message.getTargetId()));
    }

    /**
     * 偏移量
     *
     * @param timeStamp 时间戳
     * @return 偏移量
     */
    public static synchronized int offId(long timeStamp) {

        int ret = seq++;
        if (currentTimeStamp < timeStamp) {
            //当前时间戳便宜, 数据进行自增
            seq = 0;
            currentTimeStamp = timeStamp;
            ret = seq++;
        }
        if (ret > MAX_MESSAGE_SEQ) {
            // 当前时间戳用完
            nextMillis(timeStamp);
            seq = 0;
            ret = seq++;
        }
        return ret;
    }

    /**
     * 循环到下一毫秒(类阻塞)
     *
     * @param lastTimestamp 时间戳
     * @return 下一毫秒
     */
    protected static long nextMillis(long lastTimestamp) {
        // for循环阻塞到下一毫秒 / 一毫秒4000已经基本足够了
        long timestamp = getTimeStamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getTimeStamp();
        }
        return timestamp;
    }


    public static Long resolveUUID(String msgId) {

        String[] split = msgId.split("-");
        long timestamp = Long.parseLong(split[0], 16);
        int seq = Integer.parseInt(split[1]);
        return timestamp << SEQ_BIT | seq;
    }



}
