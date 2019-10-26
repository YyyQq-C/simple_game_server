package net.tcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:21.
 *
 * BinaryClientDecoder
 */
public class BinaryClientDecoder extends BaseDecoder
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        int len = in.readInt();
        byte[] str = new byte[len];
        in.readBytes(str);
        String s = new String(str);
        out.add(s);
    }
}
