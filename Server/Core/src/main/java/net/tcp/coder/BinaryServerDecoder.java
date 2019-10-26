package net.tcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:18.
 *
 * BinaryServerDecoder
 */
public class BinaryServerDecoder extends BaseDecoder
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (ctx == null || in == null)
            return;

        int len = in.readInt();
        byte[] str = new byte[len];
        in.readBytes(str);
        String s = new String(str);
        out.add(s);
    }
}
