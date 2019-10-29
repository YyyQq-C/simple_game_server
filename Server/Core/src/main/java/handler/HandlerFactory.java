package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YongQc
 *
 * 2019-10-29 15:18.
 *
 * HandlerFactory
 */
public class HandlerFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerFactory.class);

    /** handler集合 消息id,HandlerElement */
    private Map<Integer, HandlerElement> handlerMap = new HashMap<>() ;


    public static HandlerFactory getInstance()
    {
        return Singleton.INSTANCE.Instance();
    }

    private enum Singleton
    {
        INSTANCE;

        private HandlerFactory instance;

        Singleton()
        {
            instance = new HandlerFactory();
        }

        public HandlerFactory Instance()
        {
            return instance;
        }
    }
}
