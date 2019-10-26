package net.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.handler.BaseChannelInHandler;
import net.tcp.coder.BaseDecoder;
import net.tcp.coder.BaseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:20.
 *
 * TcpClient
 *
 * 基于Netty实现的TCP客户端
 *
 */
public abstract class TcpClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);

    private Bootstrap bootstrap;

    public Channel connect(String ip, int port) throws Exception
    {
        bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);

        bootstrap.handler(new ChannelInitializer<Channel>()
        {
            @Override
            protected void initChannel(Channel ch)
            {
                ch.pipeline().addLast(getDecoder());
                ch.pipeline().addLast(getEncoder());
                ch.pipeline().addLast(getChannelHandler());
            }
        });

        ChannelFuture future = bootstrap.connect(ip, port).sync().awaitUninterruptibly();
        if (future.isSuccess())
        {
            LOGGER.info("connect {}:{} SUCCESS!", ip, port);
            return future.channel();
        }
        else
        {
            LOGGER.error("connect {}:{} FAILED!", ip, port);
            stop();
            return null;
        }
    }

    public void stop()
    {
        bootstrap.config().group().shutdownGracefully();
    }

    public abstract BaseChannelInHandler getChannelHandler();

    public abstract BaseDecoder getDecoder();

    public abstract BaseEncoder getEncoder();

}
