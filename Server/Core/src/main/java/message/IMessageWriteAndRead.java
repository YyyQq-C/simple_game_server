package message;

import io.netty.buffer.ByteBuf;

/**
 * Created by YongQiancheng on 2017/11/7.
 * <p>
 * IMessageWrite
 */
public interface IMessageWriteAndRead
{
    void write(ByteBuf buf);

    void read(ByteBuf buf, int size);
}
