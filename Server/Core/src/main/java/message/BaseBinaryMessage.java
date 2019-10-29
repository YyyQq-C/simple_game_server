package message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YongQc
 *
 * 2019-10-29 14:03.
 *
 * BaseBinaryMessage
 */
public abstract class BaseBinaryMessage implements Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseBinaryMessage.class);
    private static final long serialVersionUID = -1116696599331105093L;

    // binary data io.netty.ByteBuf
    protected ByteBuf buffer;
    // message length
    protected int msgLength;

    public void writeBool(boolean boolValue)
    {
        this.buffer.writeBoolean(boolValue);
    }

    public void writeByte(int byteValue)
    {
        this.buffer.writeByte(byteValue);
    }

    public void writeShort(int shortValue)
    {
        this.buffer.writeShort(shortValue);
    }

    public void writeInt(int intValue)
    {
        this.buffer.writeInt(intValue);
    }

    public void writeLong(long longValue)
    {
        this.buffer.writeLong(longValue);
    }

    public void writeFloat(float floatValue)
    {
        this.buffer.writeFloat(floatValue);
    }

    public void writeDouble(Double doubleValue)
    {
        this.buffer.writeDouble(doubleValue);
    }

    public void writeString(String strValue)
    {
        if (strValue == null)
        {
            this.buffer.writeShort(0);
        }
        else
        {
            try
            {
                byte[] ex = strValue.getBytes("UTF-8");
                int length = ex.length;
                this.buffer.writeShort(length);
                this.buffer.writeBytes(ex);
            }
            catch (Exception ex)
            {
                LOGGER.error("encode  binary message data error,Exception :{}", ex);
            }

        }
    }

    public void writeByteArray(byte[] bytes)
    {
        if (bytes == null)
            buffer.writeShort(0);
        else
        {
            buffer.writeShort(bytes.length);
            buffer.writeBytes(bytes);
        }
    }

    public void writeBoolList(ArrayList<Boolean> boolValues)
    {
        if (boolValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = boolValues.size();
        this.buffer.writeShort(size);
        for (boolean bool : boolValues)
        {
            this.buffer.writeBoolean(bool);
        }
    }

    public void writeByteList(ArrayList<Byte> byteValues)
    {
        if (byteValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = byteValues.size();
        this.buffer.writeShort(size);
        for (byte _byte : byteValues)
        {
            this.buffer.writeByte(_byte);
        }
    }

    public void writeShortList(ArrayList<Short> shortValues)
    {
        if (shortValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = shortValues.size();
        this.buffer.writeShort(size);
        for (short _short : shortValues)
        {
            this.buffer.writeShort(_short);
        }
    }

    public void writeIntList(ArrayList<Integer> intValues)
    {
        if (intValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = intValues.size();
        this.buffer.writeShort(size);
        for (int _int : intValues)
        {
            this.buffer.writeInt(_int);
        }
    }

    public void writeLongList(ArrayList<Long> longValues)
    {
        if (longValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = longValues.size();
        this.buffer.writeShort(size);
        for (long _long : longValues)
        {
            this.buffer.writeLong(_long);
        }
    }

    public void writeFloatList(ArrayList<Float> floatValues)
    {
        if (floatValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = floatValues.size();
        this.buffer.writeShort(size);
        for (float _float : floatValues)
        {
            this.buffer.writeFloat(_float);
        }
    }

    public void writeDoubleList(ArrayList<Double> doubleValues)
    {
        if (doubleValues == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = doubleValues.size();
        this.buffer.writeShort(size);
        for (double _double : doubleValues)
        {
            this.buffer.writeDouble(_double);
        }
    }

    public void writeStringList(ArrayList<String> stringValue)
    {
        if (stringValue == null)
        {
            this.buffer.writeShort(0);
            return;
        }
        int size = stringValue.size();
        this.buffer.writeShort(size);
        for (String _string : stringValue)
        {
            this.writeString(_string);
        }
    }

    public int getStringByteSize(String value)
    {
        if (value == null)
        {
            return 0;
        }
        try
        {
            byte[] ex = value.getBytes("UTF-8");
            return ex.length;
        }
        catch (Exception e)
        {
            LOGGER.error("resolve  binary message data error,Exception :{}", e);
            return 0;
        }
    }

    public boolean readBool()
    {
        return this.buffer.readBoolean();
    }

    public byte readByte()
    {
        //
        return this.buffer.readByte();
    }

    public short readUnsignedByte()
    {
        //
        return this.buffer.readUnsignedByte();
    }

    public short readShort()
    {
        return this.buffer.readShort();
    }

    public int readInt()
    {
        return this.buffer.readInt();
    }

    public float readFloat()
    {
        return this.buffer.readFloat();
    }

    public double readDouble()
    {
        return this.buffer.readDouble();
    }

    public long readLong()
    {
        return this.buffer.readLong();
    }

    public String readString()
    {
        int length = this.buffer.readShort();
        byte[] str = new byte[length];
        this.buffer.readBytes(str);

        try
        {
            return new String(str, "UTF-8");
        }
        catch (Exception e)
        {
            LOGGER.error("resolve  binary message data error,Exception :{}", e);
            return new String(str);
        }
    }

    public byte[] readByteArray()
    {
        int length = buffer.readShort();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    public ArrayList<Boolean> readBoolList(ArrayList<Boolean> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(Boolean.valueOf(this.buffer.readBoolean()));
        }
        return out;
    }

    public ArrayList<Byte> readByteList(ArrayList<Byte> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.buffer.readByte());
        }
        return out;
    }

    public ArrayList<Short> readShortList(ArrayList<Short> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.buffer.readShort());
        }
        return out;
    }

    public ArrayList<Integer> readIntList(ArrayList<Integer> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.buffer.readInt());
        }
        return out;
    }

    public ArrayList<Long> readLongList(ArrayList<Long> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.buffer.readLong());
        }
        return out;
    }

    public ArrayList<Float> readFloatList(ArrayList<Float> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.buffer.readFloat());
        }
        return out;
    }

    public ArrayList<Double> readDoubleList(ArrayList<Double> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.buffer.readDouble());
        }
        return out;
    }

    public ArrayList<String> readStringList(ArrayList<String> out)
    {
        if (out == null)
            out = new ArrayList<>();
        int length = this.buffer.readShort();
        for (int i = 0; i < length; i++)
        {
            out.add(this.readString());
        }
        return out;
    }

    protected void write(ByteBuf byteBuf)
    {
        this.buffer = byteBuf;
        write();
        this.buffer = null;
    }

    protected abstract void write();

    public void read(ByteBuf byteBuf)
    {
        this.buffer = byteBuf;
        read();
        this.buffer = null;
    }

    protected abstract void read();

    /**
     * 将消息实体转换成 ByteBuf
     *
     * @return
     */
    public ByteBuf writeToByteBuf()
    {
        buffer = Unpooled.buffer();
        write();
        return buffer;
    }
}
