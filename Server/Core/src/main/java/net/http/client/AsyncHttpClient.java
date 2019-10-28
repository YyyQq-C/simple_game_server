package net.http.client;

import constant.SystemConstant;
import net.handler.HttpHandler;
import net.handler.HttpPostHandler;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by YongQc
 *
 * 2019-10-28 15:03.
 *
 * AsyncHttpClient
 *
 * 异步HTTP客户端
 *
 * 单独的一个线程
 *
 */
public class AsyncHttpClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpClient.class);
    /** 连接超时时间 毫秒*/
    private static final int CONNECT_TIMEOUT = 3000;
    /** socket超时时间 毫秒*/
    private static final int SOCKET_TIMEOUT = 1500;
    /** http客户端 */
    private CloseableHttpAsyncClient client;

    /** 单独单线程 */
    private Thread thread;
    /** 线程运行状态 */
    private AtomicBoolean off = new AtomicBoolean(false);
    /** 消息队列 */
    private LinkedBlockingQueue<HttpHandler> queue = new LinkedBlockingQueue<>();

    public void start()
    {
        start(Math.max(SystemConstant.CORE_NUM, 2));
    }

    /**
     * 启动异步http客户端
     * @param threadCount io线程数 建议与异步http使用的线程数一致
     */
    public void start(int threadCount)
    {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        client = HttpAsyncClients.custom().setDefaultRequestConfig(config)
                .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(threadCount).build()).build();

        client.start();
        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                execute();
            }
        });
        off.set(false);
        thread.start();
    }

    /**
     * 请求
     * @param httpHandler
     */
    public void request(HttpHandler httpHandler)
    {
        // 线程已停止 停止接收handler
        if (off.get())
            return;

        try
        {
            queue.put(httpHandler);
        }
        catch (InterruptedException e)
        {
            // 被打断
            LOGGER.error("handler放入时发生异常. handler:{} e:{}", httpHandler, e);
        }
    }

    /**
     * 队列任务执行
     */
    private void execute()
    {
        while (!off.get())
        {
            HttpHandler handler;
            if ((handler = queue.poll()) != null)
            {
                _executeHandler(handler);
            }
            else
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    // 不处理
                }
            }
        }
    }

    /**
     * 执行handler
     * @param handler
     */
    private void _executeHandler(HttpHandler handler)
    {
        HttpRequestBase base;
        if (handler instanceof HttpPostHandler)
        {
            base = new HttpPost(handler.getUrl());
            if (((HttpPostHandler) handler).getHttpEntity() != null)
                ((HttpPost) base).setEntity(((HttpPostHandler) handler).getHttpEntity());
        }
        else base = new HttpGet(handler.getUrl());

        if (handler.getHeaders() != null)
        {
            for (Header header : handler.getHeaders())
            {
                base.addHeader(header);
            }
        }
        try
        {
            client.execute(base, handler.callback());
        }
        catch (Exception e)
        {
            LOGGER.error("Http 请求发送异常.url:{} e:{}", handler.getUrl(), e);
        }
    }

    public void stop()
    {
        // 已经停止了
        if (off.get())
            return;

        // 先停止线程
        off.set(true);

        try
        {
            // 等待thread完毕
            thread.join();
        }
        catch (InterruptedException e)
        {
            LOGGER.error("等待http线程失败. e:{}", e);
        }

        // 等待队列消息执行完毕
        while (!queue.isEmpty())
        {
            _executeHandler(queue.poll());
        }

        try
        {
            client.close();
        }
        catch (IOException e)
        {
            // 不需要处理 只需要记录日志
            LOGGER.error("异步HTTP停止发生异常. e:{}", e);
        }

    }

}
