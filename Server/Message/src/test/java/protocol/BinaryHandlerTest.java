package protocol;

import binary.Test;
import handler.Handler;

/**
 * Created by YongQc
 *
 * 2019-10-30 11:31.
 *
 * BinaryHandlerTest
 */
public class BinaryHandlerTest extends Handler
{
    @Override
    public void action()
    {
        Test.ReqHello hello = getMsg();

        System.out.println(hello.getCommandType());
        System.out.println(hello.getParameters());
    }
}
