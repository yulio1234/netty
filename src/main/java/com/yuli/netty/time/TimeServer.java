package com.yuli.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 基于netty到时间服务
 */
public class TimeServer {
    public void bind(int port) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup wokerGroup = new NioEventLoopGroup();
        final TimeServerHandler timeServerHandler = new TimeServerHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,wokerGroup)
                    //请求链接处理
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    //具体事件处理，链接被注册之后调用管道初始化方法
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(timeServerHandler);
                        }
                    });
            //异步绑定端口，调用sync()方法等待知道绑定完成
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            //阻塞，等服务端链路关闭后main函数才退出
            sync.channel().closeFuture().sync();
        } finally {
            //释放资源
            bossGroup.shutdownGracefully();
            wokerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new TimeServer().bind(port);
    }
}
