package com.yuli.netty.protobuf;

import com.yuli.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<DataInfo.Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Message msg) throws Exception {
        DataInfo.Message.DataType dataType = msg.getDataType();
        if(dataType == DataInfo.Message.DataType.PersonType){
            DataInfo.Person person = msg.getPerson();
            System.out.println(person.getName());
            System.out.println(person.getAge());
            System.out.println(person.getAddress());
        }else if(dataType == DataInfo.Message.DataType.DogType){
            DataInfo.Dog dog = msg.getDog();
            System.out.println(dog.getName());
            System.out.println(dog.getAge());
        }else if(dataType == DataInfo.Message.DataType.CatType){
            System.out.println(msg.getCat().getName());
            System.out.println(msg.getCat().getAge());
        }
    }
}
