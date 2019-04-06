package com.bihang.seaya.bootstrap;

import com.bihang.seaya.bean.SeayaBeanManager;
import com.bihang.seaya.config.AppConfig;
import com.bihang.seaya.configuration.AbstractSeayaConfiguration;
import com.bihang.seaya.configuration.ApplicationConfiguration;
import com.bihang.seaya.configuration.ConfigurationHolder;
import com.bihang.seaya.constant.SeayaConstant;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.init.SeayaInitializer;
import com.bihang.seaya.log.SeayaLog;
import com.bihang.seaya.thread.ThreadLocalHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bihang.seaya.constant.SeayaConstant.SystemProperties.APPLICATION_THREAD_SHUTDOWN_NAME;
import static com.bihang.seaya.constant.SeayaConstant.SystemProperties.APPLICATION_THREAD_WORK_NAME;

public class NettyBootStrap {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyBootStrap.class);

    private static AppConfig appConfig = AppConfig.getInstance() ;
    private static EventLoopGroup boss = new NioEventLoopGroup(1,new DefaultThreadFactory("boss"));
    private static EventLoopGroup work = new NioEventLoopGroup(0,new DefaultThreadFactory(APPLICATION_THREAD_WORK_NAME));
    private static Channel channel ;

    /**
     * Start netty Server
     *
     * @throws Exception
     */
    public static void startSeaya() throws Exception {
        // start
        startServer();

        // register shutdown hook
        shutDownServer();

        // synchronized channel
        joinServer();
    }

    /**
     * start netty server
     * @throws InterruptedException
     */
    private static void startServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SeayaInitializer());

        ChannelFuture future = bootstrap.bind(AppConfig.getInstance().getPort()).sync();
        if (future.isSuccess()) {
            appLog();
        }
        channel = future.channel();
    }

    private static void joinServer() throws Exception {
        channel.closeFuture().sync();
    }

    private static void appLog() {
        Long start = ThreadLocalHolder.getLocalTime();
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration)ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);
        long end = System.currentTimeMillis();
        LOGGER.info("Seaya started on port: {}.cost {}ms", applicationConfiguration.get(SeayaConstant.SEAYA_PORT), end - start);
        LOGGER.info(">> access http://{}:{}{} <<","127.0.0.1",appConfig.getPort(),appConfig.getRootPath());
    }

    /**
     * shutdown server
     */
    private static void shutDownServer() {
        ShutDownThread shutDownThread = new ShutDownThread();
        shutDownThread.setName(APPLICATION_THREAD_SHUTDOWN_NAME);
        Runtime.getRuntime().addShutdownHook(shutDownThread);
    }

    private static class ShutDownThread extends Thread {
        @Override
        public void run() {
            SeayaLog.log("Seaya server stop...");
            SeayaContext.removeContext();

            SeayaBeanManager.getInstance().releaseBean();

            boss.shutdownGracefully();
            work.shutdownGracefully();

            SeayaLog.log("Seaya server has been successfully stopped.");
        }

    }
}
