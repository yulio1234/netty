package com.yuli.netty.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.UUID;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连上了");
        ctx.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("客户端成功关闭");
                }
            }
        });
        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端channel取消注册");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端关闭了");
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+":"+message.getContext());
        message.setContext("from server:"+ UUID.randomUUID());
        ctx.channel().writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务器发现异常");
        cause.printStackTrace();
        ctx.close();
    }
}
