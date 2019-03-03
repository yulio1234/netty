package com.yuli.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件，
 * MappedByteBuffer，在内存中进行修改，通过操作系统同步到文件
 */
public class NioTest9 {
    public static void main(String[] args) throws IOException {
        //随机访问文件,可以跳到文件任意位置进行读写
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        //将文件映射到内存中
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte)'a');
        mappedByteBuffer.put(3,(byte)'b');
        randomAccessFile.close();
    }
}
