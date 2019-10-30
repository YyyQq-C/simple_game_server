package thread;

import handler.Handler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by YongQc
 *
 * 2019-10-30 13:44.
 *
 * BaseProcessor
 * 线程基类 提供线程池构造
 */
public class BaseProcessor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseProcessor.class);

    private ExecutorService executor;

    public BaseProcessor(String name) throws IllegalAccessException
    {
        this(name, 1);
    }

    /**
     * 构造
     * @param name 线程名
     * @param threadNum 线程数
     * @throws IllegalAccessException
     */
    public BaseProcessor(String name, int threadNum) throws IllegalAccessException
    {
        if (threadNum < 1)
        {
            throw new IllegalAccessException("创建的线程数不能小于1.");
        }

        executor = Executors.newFixedThreadPool(threadNum, new DefaultThreadFactory(name));
    }

    /**
     * 投递handler
     * @param handler
     * @return 返回执行结果
     */
    public Future<?> submitHandler(Handler handler)
    {
        // 线程已停止
        if (executor.isShutdown())
            return null;

        if (handler == null)
            return null;

        return executor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                executeHandler(handler);
            }
        });
    }

    /**
     * 执行handler
     * @param handler
     */
    protected void executeHandler(Handler handler)
    {
        try
        {
            handler.action();
        }
        catch (Exception e)
        {
            LOGGER.error("执行handler发生异常.", e);
        }
    }
}
