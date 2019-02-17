package com.yuli.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest2 {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("NioTest2");
        FileChannel channel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        channel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            System.out.println("Character:" + (char) b);
        }
        inputStream.close();
    }
}
