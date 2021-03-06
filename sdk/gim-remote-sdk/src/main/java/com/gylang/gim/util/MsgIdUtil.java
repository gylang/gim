package com.gylang.gim.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.gylang.gim.api.domain.common.MessageWrap;

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
public class MsgIdUtil  {

    private static final MsgIdUtil msgIdUtil = new MsgIdUtil();

    public static MsgIdUtil getMsgIdUtil() {
        return msgIdUtil;
    }

    private MsgIdUtil() {
    }


    private static Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    /**
     * 16^3 -1 = 4905
     */
    private static final int MAX_MESSAGE_SEQ = 0xFFF;
    private static final int SEQ_BIT = Integer.bitCount(MAX_MESSAGE_SEQ);
    /**
     * 当前自增序列号
     */
    private static int seq = 0;
    /**
     * 当前时间戳
     */
    private static long currentTimeStamp = 0L;

    private static final int TIMESTAMP_CALCULATE = 64 - 41;

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

        // 使用雪花算法 保持系统全局递增趋势
        long nextId = snowflake.nextId();
        message.setTimeStamp(nextId);
        String s = Long.toString(nextId);
        message.setClientMsgId(s);
        return s;
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

    public static Long timestamp(long snowflake) {

        // 去除数据中心 序列号 影响 获取最原始的时间戳 可以用户查询最新消息
        return (snowflake >> TIMESTAMP_CALCULATE) << TIMESTAMP_CALCULATE;
    }



}
