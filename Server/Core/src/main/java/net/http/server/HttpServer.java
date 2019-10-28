package net.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.handler.BaseChannelInHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by YongQc
 *
 * 2019-10-28 09:43.
 *
 * HttpServer
 */
public abstract class HttpServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    /** 接收最大内容 1M */
    private static final int MAX_CONTENT_LEN = 1 << 20;
    /** 空闲检测时间 秒 */
    private static final int IDLE_TIME = 5;

    private ServerBootstrap bootstrap;


    /**
     * 启动HTTP服务器
     * @param port 监听的端口
     * @throws Exception
     */
    public void start(int port) throws Exception
    {
        bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(5)).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 3000)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch)
                    {
                        ch.pipeline()
                                .addLast(new HttpServerCodec())
                                // 将同一多个http请求组合成一个http请求
                                .addLast(new HttpObjectAggregator(MAX_CONTENT_LEN))
                                .addLast(new IdleStateHandler(IDLE_TIME, IDLE_TIME, IDLE_TIME, TimeUnit.SECONDS))
                                .addLast(getChannelHandler());
                    }
                });

        ChannelFuture future = bootstrap.bind(port).sync().awaitUninterruptibly();
        if (future.isSuccess())
        {
            LOGGER.info("Http server start SUCCESS! port:{}", port);
        }
        else
        {
            stop();
            throw new Exception("Http Server start FAILED, cause:" + future.cause());
        }

        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>()
        {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception
            {
                LOGGER.info("Http server CLOSED!");
            }
        });
    }

    public void stop()
    {
        bootstrap.config().childGroup().shutdownGracefully();
        bootstrap.config().group().shutdownGracefully();
    }


    public abstract BaseChannelInHandler getChannelHandler();

}
