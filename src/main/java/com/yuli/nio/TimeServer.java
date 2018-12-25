package com.yuli.nio;

/**
 * nio时间服务
 */
public class TimeServer {
    public static void main(String[] args) {
        new Thread(new MultiplexerTimeServer(8080),"NIO-MultiplexerTimeServer-001").start();
    }
}
