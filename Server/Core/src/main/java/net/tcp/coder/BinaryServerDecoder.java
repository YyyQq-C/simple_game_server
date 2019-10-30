package net.tcp.coder;

import handler.Handler;
import handler.HandlerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryServerDecoder.class);
    /** 包序列号 */
    private static final AttributeKey<Integer> MSG_ORDER = AttributeKey.valueOf("msg_order");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (ctx == null || in == null)
            return;

        // 消息不全 继续等待
        if (in.readableBytes() < 4)
            return;

        // 包大小
        int len = in.getInt(4);
        // todo 设置包最大值

        if (in.readableBytes() < len)
            return;

        // 消息结构组成 （消息长度[int] + 消息序列号[int] + 消息id[int] + 消息体[bytebuf]）
        // 消息长度 读指针移位
        in.readInt();

        // 消息虚拟号验证
        int msgOrder = in.readInt();
        if (ctx.channel().attr(MSG_ORDER) != null && ctx.channel().attr(MSG_ORDER).get() + 1 != msgOrder)
        {
            ctx.close();
            return;
        }

        ctx.channel().attr(MSG_ORDER).set(msgOrder);
        // 消息id
        int msgID = in.readInt();
        try
        {
            Handler handler = HandlerFactory.getInstance().parseHandler(msgID, in);
            if (handler != null)
                out.add(handler);
        }
        catch (Exception e)
        {
            LOGGER.error("消息解码器解码消息发生异常.", e);
        }
    }
}
