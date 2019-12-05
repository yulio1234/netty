package com.yuli.netty.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new Encode());
                            ch.pipeline().addLast(new Decode());
                            ch.pipeline().addLast(new ClientHandler());

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("链接成功");
                    }else {
                        System.out.println("链接失败");
                        future.cause().printStackTrace();
                    }
                }
            });
            channelFuture.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("客户端关闭了");
                    }
                }
            });
            Thread.sleep(1000);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
