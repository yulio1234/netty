package com.yuli.netty.protobuf;

import com.yuli.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class ClientHandler extends SimpleChannelInboundHandler<DataInfo.Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Message msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DataInfo.Message message = null;
        int i = new Random().nextInt(3);
        if (0 == i) {
            message = DataInfo.Message.newBuilder()
                    .setDataType(DataInfo.Message.DataType.PersonType)
                    .setPerson(DataInfo.Person.newBuilder().setAddress("chengdu").setAge(15).setName("yuli").build()).build();
        }else if(1 == i){
            message = DataInfo.Message.newBuilder()
                    .setDataType(DataInfo.Message.DataType.DogType)
                    .setDog(DataInfo.Dog.newBuilder().setAge(2).setName("kaka").build()).build();
        }else if(2 == i){
            message = DataInfo.Message.newBuilder()
                    .setDataType(DataInfo.Message.DataType.CatType)
                    .setCat(DataInfo.Cat.newBuilder().setAge(1).setName("huahua").build()).build();
        }
        ctx.writeAndFlush(message);
        ctx.close();
    }
}
