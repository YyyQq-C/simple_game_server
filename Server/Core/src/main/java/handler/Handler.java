package handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by YongQc
 *
 * 2019-10-29 10:54.
 *
 * Handler
 */
public abstract class Handler
{
    private ChannelHandlerContext ctx;
    private Object msg;

    public abstract void action();

}
