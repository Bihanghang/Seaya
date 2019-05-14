package com.bihang.seaya.init;

import com.bihang.seaya.handle.HttpDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class SeayaInitializer extends ChannelInitializer<Channel> {
    private final HttpDispatcher httpDispatcher = new HttpDispatcher() ;

    @Override
    public void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new HttpRequestDecoder())
                .addLast(new HttpResponseEncoder())
                .addLast(new ChunkedWriteHandler())//该通道处理器主要是为了处理大文件传输的情形。大文件传输时，需要复杂的状态管理，
                .addLast(httpDispatcher)
                /*.addLast("logging", new LoggingHandler(LogLevel.INFO))*/;
    }

}
