package com.yuli.nio;

import java.nio.ByteBuffer;

/**
 * 创建slice对象，共享数据制定范围的底层数组
 */
public class NioTest6 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        buffer.position(2);
        buffer.limit(6);
        ByteBuffer slice = buffer.slice();
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 2;
            slice.put(i,b);
        }
        buffer.position(0);

        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
