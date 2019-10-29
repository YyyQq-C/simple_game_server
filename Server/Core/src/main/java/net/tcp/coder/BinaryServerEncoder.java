package net.tcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import message.BinaryMessage;

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
        if (msg instanceof BinaryMessage)
        {
            BinaryMessage smsg = (BinaryMessage) msg;
            // 消息结构组成 （消息长度[int + 消息id[int] + 消息体[bytebuf]）
            ByteBuf msgBuf = smsg.writeToByteBuf();
            out.writeInt(4 + msgBuf.writerIndex());
            out.writeInt(smsg.getMsgId());
            // todo 是否压缩
            out.writeBytes(msgBuf);

            // 释放
            msgBuf.release();
        }
    }
}
