package com.gylang.netty.sdk.coder;

import com.gylang.netty.sdk.conveter.MessageConverterAdapter;
import com.gylang.netty.sdk.domain.GylHeader;
import com.gylang.netty.sdk.domain.MessageWrap;
import io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * |    2 协议类型    |1 version | 1 magic |
 * |    2 序列化算法   |        2 服务类型   |
 * |              4 数据长度               |
 *
 * @author gylang
 * data 2020/12/7
 */
public class SimpleGylProtocolDecoder implements BaseProtocolProcess<ByteBuf, MessageWrap> {

    private static final int HEADER_SIZE = 8;

    public static final int PROTOCOL = 2;

    public static final int VERSION = 1;

    public static final int MAGIC = 1;

    public static final int SERIALIZE = 1;

    public static final int SERVER_TYPE = 1;

    public static final int PROTOCOL_LOGO = 1;

    private Map<Byte, MessageConverterAdapter<ByteBuf>> messageConverterAdapterMap = new ConcurrentHashMap<>();

    @Override
    public MessageWrap decode(ByteBuf byteBuf) {

        if (byteBuf == null) {
            return null;
        }

        // 消息头读取不完整，不做解析返回null，直到读完整为止
        if (byteBuf.readableBytes() <= HEADER_SIZE) {
            return null;
        }

        byteBuf.markReaderIndex();
        // 协议类型
        short protocolType = byteBuf.readShort();
        byte version = byteBuf.readByte();
        byte magic = byteBuf.readByte();
        byte serialize = byteBuf.readByte();
        short serverType = byteBuf.readShort();
        int length = byteBuf.readInt();
        GylHeader header = new GylHeader();
        header.setProtocolType(protocolType);
        header.setVersion(version);
        header.setMagic(magic);
        header.setServerType(serverType);
        header.setLength(length);
        // TODO 网络信号不好，没有接收到完整数据
        if (byteBuf.readableBytes() < length) {
            //保存当前读到的数据，下一次继续读取
            //断包处理：查看ByteToMessageDecoder的channelRead方法，ByteBuf cumulation属性
            byteBuf.resetReaderIndex();
            return null;
        }
        byte[] data = new byte[length];
        ByteBuf dataBytes = byteBuf.readBytes(data);
        MessageConverterAdapter<ByteBuf> converterAdapter = messageConverterAdapterMap.get(serialize);
        if (null != converterAdapter) {
            MessageWrap messageWrap = converterAdapter.resolve(dataBytes, MessageWrap.class);
            messageWrap.setHeader(header);
            // or ReferenceCountUtil.release(in);
            byteBuf.release();
            return messageWrap;
        }

        byteBuf.release();
        // TODO 手动释放内存
        return null;
    }


    @Override
    public ByteBuf encode(MessageWrap messageWrap) {
        return null;
    }
}
