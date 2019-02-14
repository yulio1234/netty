package com.yuli.netty.protobuf;

import com.yuli.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<DataInfo.Person> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Person msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DataInfo.Person person = DataInfo.Person.newBuilder().setAge(18).setAddress("aaa").setName("yuli").build();
        ctx.writeAndFlush(person);
    }
}
