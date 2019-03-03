package com.yuli.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 验证clear方法，注释之后position和limit位置重叠，会一直读到0，循环输出
 */
public class NioTest8 {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("Input.txt");
        FileOutputStream outputStream = new FileOutputStream("Output.txt");
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        while (true) {
            //注释掉buffer之后，会死循环，因为position和limit位置重叠，无法读入数据
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
