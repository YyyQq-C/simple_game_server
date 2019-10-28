import io.netty.util.CharsetUtil;
import net.handler.HttpHandler;
import net.http.client.AsyncHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by YongQc
 *
 * 2019-10-28 19:06.
 *
 * AsyncHttpTest
 */
public class AsyncHttpTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpTest.class);
    public static void main(String[] args)
    {
        String userPath = System.getProperty("user.dir");
        String configPath = new StringBuilder(userPath).append(File.separator).append("config").append(File.separator).toString();
        System.setProperty("log4j.configurationFile", configPath + "log4j2_devel.xml");

        AsyncHttpClient client = new AsyncHttpClient();
        client.start(2);

        client.request(new HttpHandler("http://127.0.0.1:23456/?command=2")
        {
            @Override
            public void completed(HttpResponse httpResponse)
            {
                try
                {
                    String res = EntityUtils.toString(httpResponse.getEntity(), CharsetUtil.UTF_8);
                    System.out.println(res);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}
