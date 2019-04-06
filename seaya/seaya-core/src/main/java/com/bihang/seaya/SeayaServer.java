package com.bihang.seaya;

import com.bihang.seaya.bootstrap.NettyBootStrap;
import com.bihang.seaya.config.SeayaSetting;
import com.bihang.seaya.environment.Environment;
import com.bihang.seaya.handler.SeayaHandler;
import com.bihang.seaya.log.PrintSeayaLogo;
import com.bihang.seaya.log.SeayaLog;
import com.bihang.seaya.text.TextHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * Created By bihang
 * 2018/12/21 17:03
 */
@Slf4j
public class SeayaServer {

    /**
     * 通过指定指定路径启动Seaya
     * @param clazz
     * @param path
     * @throws Exception
     */
    public static void start(Class<?> clazz,String path) throws Exception {
        SeayaSetting.setting(clazz,path) ;
        NettyBootStrap.startSeaya();
    }


    /**
     * Start the service through the port in the configuration file
     * @param clazz
     * @throws Exception
     */
    public static void start(Class<?> clazz) throws Exception {
        start(clazz,null);
    }
}
