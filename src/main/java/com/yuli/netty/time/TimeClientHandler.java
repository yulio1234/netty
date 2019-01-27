package com.yuli.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Auther: yuli
 * @Date: 2018/12/25 15:08
 * @Description:
 */
public class TimeClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final ByteBuf firstMessage;
    public TimeClientHandler(){
        byte[] request = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(request.length);
        firstMessage.writeBytes(request);
    }

    /**
     * 当客户端和服务端TCP链路建立成功之后，netty的NIO线程会调用channelActive方法，发送查询执行给服务端。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送查询时间的请求给服务端
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端返回应答消息时，该方法被调用，读取并打印消息
     * @param ctx
     * @param byteBuf
     * @throws Exception
     */


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request);
        String body = new String(request, CharsetUtil.UTF_8);
        System.out.println("现在的时间是：" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
