package protocol;


import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by YongQc
 *
 * 2019-10-29 10:48.
 *
 * ProtocolTest
 */
public class ProtocolTest
{
    public static void main(String[] args) throws InvalidProtocolBufferException
    {
        Hello.ReqHelloWorld.Builder builder = Hello.ReqHelloWorld.newBuilder();
        builder.setId(1);
        builder.setName("name");
        builder.build();
        Hello.ResHelloWorld world = Hello.ResHelloWorld.parseFrom(new byte[2]);
    }
}
