package com.yuli.netty.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encode extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(message.getMagic());
        byte[] bytes = message.getContext().getBytes();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
