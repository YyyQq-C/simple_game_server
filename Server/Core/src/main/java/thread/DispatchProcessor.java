package thread;

import handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YongQc
 *
 * 2019-10-30 13:43.
 *
 * DispatchProcessor
 *
 * 消息分发线程
 *
 */
public class DispatchProcessor extends BaseProcessor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchProcessor.class);


    @Override
    protected void executeHandler(Handler handler)
    {

        try
        {
            handler.action();
        }
        catch (Exception e)
        {

        }

    }

    public DispatchProcessor() throws IllegalAccessException
    {
        super("Dispatch-processor");
    }

    public static DispatchProcessor getInstance()
    {
        return Singleton.INSTANCE.Instance();
    }

    private enum Singleton
    {
        INSTANCE;

        private DispatchProcessor instance;

        Singleton()
        {
            try
            {
                instance = new DispatchProcessor();
            }
            catch (IllegalAccessException e)
            {
                LOGGER.error("实例化分发线程异常.", e);
            }
        }

        public DispatchProcessor Instance()
        {
            return instance;
        }
    }
}
