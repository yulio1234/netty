package com.yuli.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过nio进行文件读写
 */
public class NioTest4 {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("Input.txt");
        FileOutputStream outputStream = new FileOutputStream("Output.txt");
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        while (true) {
            buffer.clear();
            //返回实际读到字节个数
            int read = inputStreamChannel.read(buffer);
            System.out.println(read);
            if(-1 == read){
                break;
            }
            buffer.flip();
            outputStreamChannel.write(buffer);
        }
        inputStream.close();
        outputStream.close();
    }
}
