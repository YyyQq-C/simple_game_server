package handler;

/**
 * Created by YongQc
 *
 * 2019-10-29 16:00.
 *
 * HandlerElement
 */
public class HandlerElement
{
    private Class message;
    private Class<? extends Handler> handler;

    public HandlerElement(Class<?> message, Class<? extends Handler> handler)
    {
        this.message = message;
        this.handler = handler;
    }
}
