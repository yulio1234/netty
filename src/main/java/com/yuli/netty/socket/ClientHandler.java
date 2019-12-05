package com.yuli.netty.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message = new Message();
        message.setMagic((byte)111);
        message.setContext("hello");
        ctx.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output:" + msg.getContext());
        Message message = new Message();
        message.setContext("from client:"+ LocalDateTime.now());
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端发现异常");
        cause.printStackTrace();
        ctx.close();
    }
}
