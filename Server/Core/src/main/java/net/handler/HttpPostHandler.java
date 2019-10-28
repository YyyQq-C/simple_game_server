package net.handler;

import org.apache.http.HttpEntity;

/**
 * Created by YongQc
 *
 * 2019-10-28 15:16.
 *
 * HttpPostHandler
 */
public abstract class HttpPostHandler extends HttpHandler
{
    public HttpPostHandler(String url)
    {
        super(url);
    }

    /**
     * 请求体
     * @return
     */
    public abstract HttpEntity getHttpEntity();
}
