package com.yuli.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

/**
 * 时间服务多路复用类，他是一个独立的线程，负责轮询多路复用器Selctor，可以处理多个客户端的并发写入
 */
public class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port){
        try {
            //创建多路复用器
            selector = Selector.open();
            //创建服务端socket通道
            serverChannel = ServerSocketChannel.open();
            //使用异步非阻塞模块
            serverChannel.configureBlocking(false);
            //绑定服务端监听端口，并设置socket缓存队列为1024。
            serverChannel.socket().bind(new InetSocketAddress(port),1024);
            //将serverChannel 注册到多路复用器Selector上，监听OP_ACCEPT操作位，就是接入请求事件，处理tcp三次握手请求
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NIO时间服务器启动在端口：" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 停止
     */
    public void stop(){
        this.stop = true;
    }

    /**
     * 处理请求
     */
    private void handleInput(SelectionKey key) throws IOException {
        //检查key是否有效
        if (key.isValid()) {
            //处理接入请求消息
            if (key.isAcceptable()) {
                //接受新的请求
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                //接收客户端连接请求，并穿件socketChannel实例，相当于完成了TCP三次握手，TCP物理链路正式建立
                SocketChannel socketChannel = serverSocketChannel.accept();
                //设置为异步非阻塞
                socketChannel.configureBlocking(false);
                //将socketChannel注册到多路复用器上，监听读操作，读取客户端发送到网络消息
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            //处理读消息
            if(key.isReadable()){
                SocketChannel socketChannel = (SocketChannel) key.channel();
                //分配读取内存缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //将网络数据流读取到内存中,并得到数据流到大小
                int readBytes = socketChannel.read(readBuffer);
                //如果大于0，就处理数据
                if (readBytes > 0) {
                    //绕回缓冲区
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("NIO时间服务器接收到消息" + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
                    doWrite(socketChannel,currentTime);
                }else if(readBytes < 0){
                    key.channel();
                    socketChannel.close();
                }
            }
        }
    }

    /**
     * 将数据返回
     * @param socketChannel
     * @param response
     * @throws IOException
     */
    private void doWrite(SocketChannel socketChannel,String response) throws IOException {
        if(response != null){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        }
    }
    @Override
    public void run() {
        while(!stop){
            try {
                //设置唤醒时间为1秒，无论是否有事件
                selector.select(1000);
                //获取事件就绪到key
                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                //轮询就绪事件key
                SelectionKey key = null;
                while (selectionKeys.hasNext()) {
                    key = selectionKeys.next();
                    selectionKeys.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
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
