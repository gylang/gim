package com.gylang.netty.sdk.util;

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
     * @param bizKey    业务类型
     * @param sessionId 会话id 群聊id/用户id
     * @return
     */
    public static String increase(byte bizKey, long sessionId) {
        long timeStamp = getTimeStamp();
        int offId = offId(timeStamp);
        // 时间戳 + 序列化
        return String.join("-", Long.toHexString(timeStamp), Integer.toHexString(offId),
                Integer.toHexString(bizKey), Long.toHexString(sessionId));
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


}
