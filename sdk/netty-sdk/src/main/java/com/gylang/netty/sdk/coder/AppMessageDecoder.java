/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.gylang.netty.sdk.coder;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.cmd.SystemChatCmd;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 应用层协议解析器
 *
 * @author admin
 */
public class AppMessageDecoder extends ByteToMessageDecoder {


    private static final MessageWrap messageWrap = new MessageWrap();

    static {
        messageWrap.setType(ChatType.HEART);
        messageWrap.setCmd(SystemChatCmd.HEART);
    }

    @Override
    protected void decode(ChannelHandlerContext arg0, ByteBuf buffer, List<Object> queue) throws Exception {

        /*
         * 消息体不足8位，发生断包情况
         */
        if (buffer.readableBytes() < CommonConstant.DATA_HEADER_LENGTH) {
            return;
        }

        buffer.markReaderIndex();

        byte type = buffer.readByte();

        byte lv = buffer.readByte();
        byte hv = buffer.readByte();
        int length = getContentLength(lv, hv);

        /*
         * 发生断包情况，等待接收完成
         */
        if (buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            return;
        }

        byte[] dataBytes = new byte[length];
        buffer.readBytes(dataBytes);


        Object message = mappingMessageObject(dataBytes, type);

        queue.add(message);
    }

    public Object mappingMessageObject(byte[] data, byte type) {

        if (ChatType.HEART == type) {

            return messageWrap;
        }

        MessageWrap msg = JSON.parseObject(data, MessageWrap.class);
        msg.setType(type);
        return msg;
    }

    /**
     * 解析消息体长度
     * 最大消息长度为2个字节表示的长度，即为65535
     *
     * @param lv 低位1字节消息长度
     * @param hv 高位1字节消息长度
     * @return 消息的真实长度
     */
    private int getContentLength(byte lv, byte hv) {
        int l = (lv & 0xff);
        int h = (hv & 0xff);
        return l | h << 8;
    }

}
