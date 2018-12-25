package com.yuli.aio;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8088;
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler).start();
    }
}
