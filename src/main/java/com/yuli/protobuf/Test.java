package com.yuli.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public class Test {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        //打包命令 protoc --java_out=main/java main/protobuf/Person.proto。使用构建器构建对象
        DataInfo.Person build = DataInfo.Person.newBuilder().setName("yuli").setAddress("北京").setAge(18).build();
        byte[] bytes = build.toByteArray();
        DataInfo.Person person = DataInfo.Person.parseFrom(bytes);
        System.out.println(person.getName());
    }
}
