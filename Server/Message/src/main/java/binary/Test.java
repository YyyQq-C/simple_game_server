/**
* Auto generated, do not edit it
*
* 测试消息
*/
package binary;

import io.netty.buffer.ByteBuf;
import message.BinaryMessage;
import message.IMessageList;

import java.util.ArrayList;
import java.util.List;

public final class Test{
    /**
    * 请求
    */
    public final static class ReqHello extends BinaryMessage{
        public static final int MsgId = 10101;
		public int getMsgId()
        {
            return MsgId;
        }
        private String commandType_; // 命令类型
        private String parameters_; // 参数列表


       /**
        * get 命令类型
        * @return String
        */
        public String getCommandType(){
            return commandType_;
        }

       /**
        * set 命令类型
        */
        public ReqHello setCommandType(String commandType){
            if (commandType == null)
                return this;
            this.commandType_ = commandType;
            return this;
        }

       /**
        * get 参数列表
        * @return String
        */
        public String getParameters(){
            return parameters_;
        }

       /**
        * set 参数列表
        */
        public ReqHello setParameters(String parameters){
            if (parameters == null)
                return this;
            this.parameters_ = parameters;
            return this;
        }


        @Override
        protected void write(){
            writeByte((byte)3);
            writeString(this.commandType_);
            writeString(this.parameters_);
        }

        @Override
        protected void read(){
            List<Boolean> _read_field_num_list = new ArrayList<>();
            while (true)
            {
                short fieldMark = readUnsignedByte();
                if (fieldMark > 127)
                    fieldMark -= 256;

                for(int i = 0; i < 7; i++)
                {
                    _read_field_num_list.add(((byte)fieldMark & 1 << i) == 1 << i);
                }
                if(((byte)fieldMark & 1 << 7) == 0)
                    break;
            }

            do
            {
                if (_read_field_num_list.size() > 0 && _read_field_num_list.get(0))
                {
                        this.commandType_ = readString();
                }
                else
                {
                    break;
                }
                if (_read_field_num_list.size() > 1 && _read_field_num_list.get(1))
                {
                        this.parameters_ = readString();
                }
                else
                {
                    break;
                }
            }
            while (false);

        }

       /**
        * write from custom struct to buffer.
        */
        public static void writeList(ByteBuf buffer, IMessageList struct){
            int size = struct.size();
            buffer.writeShort((short)size);
            struct.write(buffer);
        }

       /**
        * read to custom struct from buffer.
        */
        public static void readList(ByteBuf buffer, IMessageList struct){
            int num = buffer.readShort();
            struct.read(buffer, num);
        }

       /**
        * read to custom struct from buffer.
        */
        public static ReqHello readBy(ByteBuf buffer){
            ReqHello _ele = new ReqHello();
            _ele.read(buffer);
			_ele.buffer = null;
            return _ele;
        }
    }

    public enum _StructEnum_
    {
        ;
        private int value;
        _StructEnum_(int value)
        {
            this.value = value;
        }

        public static _StructEnum_ getStructEnum(int value)
        {
            for (_StructEnum_ en : _StructEnum_.values())
            {
                if (en.value == value)
                    return en;
            }
            return null;
        }
    }
}
