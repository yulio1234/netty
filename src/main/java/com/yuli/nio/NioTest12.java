package com.yuli.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class NioTest12 {
    public static void main(String[] args) throws IOException {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[0] = 5001;
        ports[0] = 5002;
        ports[0] = 5003;
        ports[0] = 5004;

        Selector selector = Selector.open();
        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket socket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ports[i]);
            socket.bind(inetSocketAddress);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口：" + ports[i]);
        }


    }
}
