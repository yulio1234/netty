package com.yuli.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 文件锁
 */
public class NioTest10 {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest10.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        //从第三个位置开始锁，锁6个长度，使用共享锁
        FileLock lock = channel.lock(3,6,true);
        System.out.println("锁是否有效" + lock.isValid());
        lock.release();
    }
}
