package net.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
     */
    public void start(int port, int workThreadNum) throws InterruptedException
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
        bootstrap.group(boss, work)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_BACKLOG, 65535)
                .option(ChannelOption.SO_BACKLOG, 65535)
                .channel(NioServerSocketChannel.class);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            protected void initChannel(SocketChannel ch)
            {
                ch.pipeline().addLast(getDecoder())
                        .addLast(getEncoder())
                        .addLast(getChannelHandler());
            }
        });

        ChannelFuture sync = bootstrap.bind(port).sync();
        sync.addListener(future -> {
            if (future.isSuccess())
            {
                LOGGER.info("server start SUCCESS! port:{}", port);
            }
            else
            {
                LOGGER.error("server start FAILED!");
            }
        });
        sync.awaitUninterruptibly();
    }

    public abstract ServerChannelHandler getChannelHandler();

    public abstract BaseDecoder getDecoder();

    public abstract BaseEncoder getEncoder();
}
