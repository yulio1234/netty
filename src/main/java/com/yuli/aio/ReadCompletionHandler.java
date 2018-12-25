package com.yuli.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * 读完成处理器
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel asynchronousSocketChannel;
    public ReadCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel){
        if(asynchronousSocketChannel != null){
            this.asynchronousSocketChannel = asynchronousSocketChannel;
        }
    }
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String request = new String(body,"UTF-8");
            System.out.println("时间服务接收到命令：" + request);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(request)?new Date().toString():"BAD ORDER";
            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private void doWrite(String currentTime){
        if (currentTime != null) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            asynchronousSocketChannel.write(writeBuffer,writeBuffer,new WriteCompletionHandler(asynchronousSocketChannel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
