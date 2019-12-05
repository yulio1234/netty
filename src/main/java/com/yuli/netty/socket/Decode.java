package com.yuli.netty.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Decode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte magic = byteBuf.readByte();
        if(magic != 0x0079){
            throw new RuntimeException("解码异常，魔术不对");
        }
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        String content = new String(bytes);
        Message message = new Message();
        message.setMagic(magic);
        message.setLength(length);
        message.setContext(content);
        list.add(message);
    }
}
