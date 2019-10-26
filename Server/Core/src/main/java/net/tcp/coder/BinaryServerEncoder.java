package net.tcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:18.
 *
 * BinaryServerEncoder
 */
public class BinaryServerEncoder extends BaseEncoder
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
    {
        if (msg instanceof String)
        {
            byte[] bytes = ((String) msg).getBytes();
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }
    }
}
