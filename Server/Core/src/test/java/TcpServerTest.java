import constant.SystemConstant;
import io.netty.channel.Channel;
import net.handler.BaseChannelInHandler;
import net.handler.ClientChannelHandler;
import net.handler.ServerChannelHandler;
import net.tcp.TcpClient;
import net.tcp.TcpServer;
import net.tcp.coder.*;

import java.io.File;

/**
 * Created by YongQc
 *
 * 2019-10-26 16:09.
 *
 * TcpServerTest
 */
public class TcpServerTest
{
    public static void main(String[] args) throws Exception
    {
        String userPath = System.getProperty("user.dir");
        String configPath = new StringBuilder(userPath).append(File.separator).append("config").append(File.separator).toString();
        System.setProperty("log4j.configurationFile", configPath + "log4j2_devel.xml");
        TcpServer server = new TcpServer()
        {
            @Override
            public BaseChannelInHandler getChannelHandler()
            {
                return new ServerChannelHandler();
            }

            @Override
            public BaseDecoder getDecoder()
            {
                return new BinaryServerDecoder();
            }

            @Override
            public BaseEncoder getEncoder()
            {
                return new BinaryServerEncoder();
            }
        };

        server.start(12345, SystemConstant.CORE_NUM, 30);


        TcpClient client = new TcpClient()
        {
            @Override
            public BaseChannelInHandler getChannelHandler()
            {
                return new ClientChannelHandler();
            }

            @Override
            public BaseDecoder getDecoder()
            {
                return new BinaryClientDecoder();
            }

            @Override
            public BaseEncoder getEncoder()
            {
                return new BinaryClientEncoder();
            }
        };

        Channel connect = client.connect("127.0.0.1", 12345);
        connect.writeAndFlush("hello! i'm client!");

        connect.close();
    }

}
