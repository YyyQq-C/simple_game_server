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
    /** 连接 */
    private ChannelHandlerContext ctx;
    /** 消息 */
    private Object msg;

    public abstract void action();

    public <T> T getMsg()
    {
        return (T) msg;
    }

    public <T> void setMsg(T msg)
    {
        this.msg = msg;
    }

}
