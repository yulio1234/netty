package com.yuli.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TimeClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;
    public TimeClientHandle(String host,int port){
        this.host = host == null ?"127.0.0.1":host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 尝试与服务端建立链接
     */
    private void doConnect() throws IOException {
        //异步连接服务端，判断是否连接成功，如果连接成功，则直接注册读状态位到多路复用器中
        //如果当前没有连接成功（异步连接，返回false，说明客户端已经发送sync包，服务端没有返回ack包，物理链路还没建立）
        if (socketChannel.connect(new InetSocketAddress(host,port))) {
            socketChannel.register(selector,SelectionKey.OP_READ);
        }else{
            //想多路复用器注册OP_CONNECT状态位，监听服务端tcp到ack应答
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    /**
     * 发送请求数据
     */
    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] request = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        writeBuffer.put(request);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed");
        }

    }
    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            SocketChannel socketChannel = (SocketChannel) key.channel();
            //如果key是连接事件
            if (key.isConnectable()) {
                //如果已经完成物理链路，则注册读事件,否则停止系统
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    doWrite(socketChannel);
                }else {
                    System.exit(1);
                }
            }
            //如果是读事件
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if(readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("Now is:" + body);
                    this.stop=true;
                }else if(readBytes < 0) {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }
    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000);
                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                SelectionKey key = null;
                while (selectionKeys.hasNext()) {
                    key = selectionKeys.next();
                    selectionKeys.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
