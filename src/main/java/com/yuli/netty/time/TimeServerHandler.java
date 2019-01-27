package com.yuli.netty.time;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;


/**
 * @Auther: yuli
 * @Date: 2018/12/25 11:27
 * @Description: 时间服务处理器
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 触发读事件
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        //获取缓冲区可读字节数组
        byte[] request = new byte[byteBuf.readableBytes()];
        //将缓冲区字节数组读取到request中
        byteBuf.readBytes(request);
        String body = new String(request, "UTF-8");
        System.out.println("时间服务器接收到命令：" + body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
        //该方法并不直接将消息写入SocketChannel中，而是把待发送的消息放到发送缓冲数组中。
        ctx.write(response);
    }

    /**
     * 触发读取完成事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将发送缓冲区中的消息全部写道SocketChannel中
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                //关闭改channel
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 发生异常时关闭ChannelHandlerContext和释放资源
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
