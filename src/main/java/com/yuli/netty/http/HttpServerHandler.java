package com.yuli.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * http服务处理器
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断请求是否是http请求
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if("/favicon.ico".equals(uri.getPath())){
                return;
            }
            //创建返回数据
            ByteBuf content = Unpooled.copiedBuffer("hello word", CharsetUtil.UTF_8);
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"test/plain");
            ctx.writeAndFlush(httpResponse);
        }
    }
}
