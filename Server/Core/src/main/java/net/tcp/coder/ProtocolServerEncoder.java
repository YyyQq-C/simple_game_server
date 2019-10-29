package net.tcp.coder;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:17.
 *
 * ProtocolServerEncoder
 *
 * pb编码器
 *
 */
public class ProtocolServerEncoder extends BaseEncoder
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
    {
        if (msg instanceof Message)
        {
            Message smsg = (Message) msg;
            byte[] array = smsg.toByteArray();

            // msg hash值作为消息id
            int msgId = smsg.hashCode();

            out.writeInt(4 + array.length);
            out.writeInt(msgId);
            out.writeBytes(array);
            array = null;
        }
    }
}
