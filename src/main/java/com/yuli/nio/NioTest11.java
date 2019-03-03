package com.yuli.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * buffer的Scattering与Gathering特性
 * 读的时候依次将buffer数组读完
 * 写的时候依次把buffer数据写完
 */
public class NioTest11 {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8080);
        serverSocketChannel.socket().bind(inetSocketAddress);

        int messageLength = 2 + 3 + 4;
        ByteBuffer[] byteBuffers = new ByteBuffer[3];
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);
        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int byteRead = 0;
            while(byteRead < messageLength){
                System.out.println("读到数据了");
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead:" + byteRead);
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position:"+byteBuffer.position()+",limit:"+byteBuffer.limit())
                        .forEach(System.out::println);
            }
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());
            long bytesWritten = 0;
            while(bytesWritten < messageLength){
                long write = socketChannel.write(byteBuffers);
                bytesWritten += write;
            }
            Arrays.asList(byteBuffers).stream().forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead:" + byteRead + ",bytesWritten:" + bytesWritten + ",messageLength:" + messageLength);
        }
    }
}
