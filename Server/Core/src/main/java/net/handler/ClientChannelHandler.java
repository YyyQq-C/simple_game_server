package net.handler;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YongQc
 *
 * 2019-10-26 17:19.
 *
 * ClientChannelHandler
 */
public class ClientChannelHandler extends BaseChannelInHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if (ctx == null)
            return;

        if (msg instanceof String)
        {
            LOGGER.info("server say msg:{}", msg);
        }
    }
}
