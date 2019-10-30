package protocol;

import handler.Handler;

/**
 * Created by YongQc
 *
 * 2019-10-30 11:16.
 *
 * HandlerTest
 */
public class HandlerTest extends Handler
{
    @Override
    public void action()
    {
        Hello.ReqHelloWorld helloWorld = getMsg();
        System.out.println(helloWorld.getId());
        System.out.println(helloWorld.getName());
    }
}
