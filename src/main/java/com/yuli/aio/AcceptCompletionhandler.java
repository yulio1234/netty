package com.yuli.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 链接建立完成处理器
 */
public class AcceptCompletionhandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        //aio套接字通道完成链接后会触发本事件，继续调用accept方法，会循环触发本类事件。每当接收到一个客户端请求成功之后，再异步接收新到客户端链接。
        attachment.asynchronousServerSocketChannel.accept(attachment, this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
