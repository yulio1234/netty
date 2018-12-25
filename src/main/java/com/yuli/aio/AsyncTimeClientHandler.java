package com.yuli.aio;

import java.nio.channels.CompletionHandler;

/**
 * AIO时间服务器客户端
 */
public class AsyncTimeClientHandler implements CompletionHandler<Void,AsyncTimeServerHandler> {
    @Override
    public void completed(Void result, AsyncTimeServerHandler attachment) {

    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {

    }
}
