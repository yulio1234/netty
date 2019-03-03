package com.yuli.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 */
public class NioTest3 {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest3.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        byte[] bytes = "hello word!".getBytes();
        buffer.put(bytes);
        buffer.flip();
        channel.write(buffer);
        fileOutputStream.close();
    }
}
