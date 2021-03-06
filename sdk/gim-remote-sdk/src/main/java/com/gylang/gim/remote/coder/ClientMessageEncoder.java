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
package com.gylang.gim.remote.coder;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.common.MessageWrap;

import java.nio.ByteBuffer;


/**
 * 客户端消息发送前进行编码
 */
public class ClientMessageEncoder {

    public ByteBuffer encode(MessageWrap body) {

        byte[] data = JSON.toJSONBytes(body);

        ByteBuffer ioBuffer = ByteBuffer.allocate(data.length + CommonConstant.DATA_HEADER_LENGTH);

        ioBuffer.put(createHeader((byte) body.getType(), data.length));
        ioBuffer.put(data);
        ioBuffer.flip();

        return ioBuffer;

    }

    /**
     * 消息体最大为65535
     *
     * @param type   类型
     * @param length 长度
     * @return
     */
    private byte[] createHeader(byte type, int length) {
        byte[] header = new byte[3];
        header[0] = type;
        header[1] = (byte) (length & 0xff);
        header[2] = (byte) ((length >> 8) & 0xff);
        return header;
    }

}
