package protocol;


import binary.Test;
import handler.Handler;
import handler.HandlerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;

/**
 * Created by YongQc
 *
 * 2019-10-29 10:48.
 *
 * ProtocolTest
 */
public class ProtocolTest
{
    public static void main(String[] args) throws Exception
    {
        String userPath = System.getProperty("user.dir");
        String configPath = new StringBuilder(userPath).append(File.separator).append("config").append(File.separator).toString();
        System.setProperty("log4j.configurationFile", configPath + "log4j2_devel.xml");

        Hello.ReqHelloWorld.Builder builder = Hello.ReqHelloWorld.newBuilder();
        builder.setId(1);
        builder.setName("name");
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(builder.build().toByteArray());

        HandlerFactory.getInstance().init("config/message.xml");
        Handler handler = HandlerFactory.getInstance().parseHandler("protocol.Hello$ReqHelloWorld".hashCode(), buf);

        handler.action();


        Test.ReqHello hello = new Test.ReqHello();
        hello.setCommandType("111");
        hello.setParameters("sdf");
        Handler h1 = HandlerFactory.getInstance().parseHandler(10101, hello.writeToByteBuf());
        h1.action();
    }
}
