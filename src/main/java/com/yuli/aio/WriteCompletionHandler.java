package com.yuli.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 写完成处理器
 */
public class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel asynchronousSocketChannel;
    public WriteCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel){
        if(asynchronousSocketChannel != null){
            this.asynchronousSocketChannel = asynchronousSocketChannel;
        }
    }
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //如果消息没有发送完成，继续发送
        if (attachment.hasRemaining()) {
            asynchronousSocketChannel.write(attachment,attachment,this);
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
