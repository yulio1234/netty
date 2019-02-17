package com.yuli.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest1 {
    public static void main(String[] args) {
        //创建
        IntBuffer intBuffer = IntBuffer.allocate(10);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            int nextInt = new SecureRandom().nextInt(20);
            intBuffer.put(nextInt);
        }
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
