package com.yuli.netty.socket;

import com.yuli.netty.http.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class Server {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    //请求链接处理
                    .channel(NioServerSocketChannel.class)
                    //具体事件处理，链接被注册之后调用管道初始化方法
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new Encode());
                            ch.pipeline().addLast(new Decode());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            //异步绑定端口，调用sync()方法等待知道绑定完成
            ChannelFuture sync = serverBootstrap.bind(8080).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("服务端开始监听客户端");
                    }else {
                        System.out.println("服务端监听失败");
                    }
                }
            });
            //阻塞，等服务端链路关闭后main函数才退出
            sync.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("关闭了");
                    }else {
                        System.out.println("不知道什么意思");
                    }
                }
            }).awaitUninterruptibly();
        } finally {
            //释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
