package net.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.handler.ServerChannelHandler;
import net.tcp.coder.BaseDecoder;
import net.tcp.coder.BaseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:16.
 *
 * TcpServer
 *
 * 基于Netty实现的TCP服务器
 *
 */
public abstract class TcpServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    private ServerBootstrap bootstrap;

    /**
     * 启动TCP监听
     * @param port 监听端口
     * @param workThreadNum work工作线程数
     * @param idleTime 读写空闲时间
     */
    public void start(int port, int workThreadNum, int idleTime) throws Exception
    {
        if (bootstrap != null)
        {
            LOGGER.warn("server has started!");
            return;
        }

        // boss线程使用1个即可
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup(workThreadNum);

        bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_BACKLOG, 65535)
                // 使用缓存池
                .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                .option(ChannelOption.SO_BACKLOG, 65535)
                .option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true));

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            protected void initChannel(SocketChannel ch)
            {
                ch.pipeline().addLast(getDecoder())
                        .addLast(getEncoder())
                        .addLast(getChannelHandler());
                if (idleTime > 0)
                {
                    // 添加空闲连接的检测
                    ch.pipeline().addLast(new IdleStateHandler(idleTime, idleTime, 0));
                }
            }
        });

        // 启动监听
        ChannelFuture future = bootstrap.bind(port).sync();
        // 同步等待
        future.awaitUninterruptibly();
        if (future.isSuccess())
        {
            LOGGER.info("server start SUCCESS! port:{}", port);
            // 监听停止
            future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>()
            {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception
                {
                    LOGGER.info("TCP SERVER HAS BEAN CLOSED! TCP PORT:{}", port);
                }
            });
        }
        else
        {
            stop();
            throw new Exception("server start FAILED! Ex:" + future.cause());
        }
    }

    /**
     * 关闭服务器
     */
    public void stop()
    {
        bootstrap.config().group().shutdownGracefully();
        bootstrap.config().childGroup().shutdownGracefully();
    }

    /**
     * 服务器应答器
     * @return
     */
    public abstract ServerChannelHandler getChannelHandler();

    /**
     * 服务器解码器
     * @return
     */
    public abstract BaseDecoder getDecoder();

    /**
     * 服务器编码器
     * @return
     */
    public abstract BaseEncoder getEncoder();
}
