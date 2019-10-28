import io.netty.handler.codec.http.HttpHeaders;
import net.handler.BaseChannelInHandler;
import net.handler.HttpServerChannelHandler;
import net.http.server.HttpServer;

import java.io.File;
import java.util.Map;

/**
 * Created by YongQc
 *
 * 2019-10-28 09:59.
 *
 * HttpServerTest
 */
public class HttpServerTest
{
    public static void main(String[] args) throws Exception
    {
        String userPath = System.getProperty("user.dir");
        String configPath = new StringBuilder(userPath).append(File.separator).append("config").append(File.separator).toString();
        System.setProperty("log4j.configurationFile", configPath + "log4j2_devel.xml");

        HttpServer server = new HttpServer()
        {
            @Override
            public BaseChannelInHandler getChannelHandler()
            {
                return new HttpServerChannelHandler()
                {
                    @Override
                    public String dispose(String ip, HttpHeaders headers, Map<String, Object> param)
                    {
                        System.out.println(ip);
                        System.out.println(headers.toString());
                        param.entrySet().forEach(key -> System.out.println(key.toString()));

                        return "OK";
                    }
                };
            }
        };

        server.start(23456);
    }
}
