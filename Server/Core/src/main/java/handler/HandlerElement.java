package handler;

import message.BinaryMessage;

/**
 * Created by YongQc
 *
 * 2019-10-29 16:00.
 *
 * HandlerElement
 */
public class HandlerElement
{
    private Class<? extends BinaryMessage> message;
    private Class<? extends Handler> handler;
}
