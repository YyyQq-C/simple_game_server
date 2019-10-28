package net.handler;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YongQc
 *
 * 2019-10-28 15:38.
 *
 * HttpHandler
 */
public abstract class HttpHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHandler.class);

    private String url;
    private Header[] headers;

    public HttpHandler(String url)
    {
        this.url = url;
    }

    public FutureCallback<HttpResponse> callback()
    {
        return new FutureCallback<HttpResponse>()
        {
            @Override
            public void completed(HttpResponse httpResponse)
            {
                HttpHandler.this.completed(httpResponse);
            }

            @Override
            public void failed(Exception e)
            {
                HttpHandler.this.failed(e);
            }

            @Override
            public void cancelled()
            {
                HttpHandler.this.cancelled();
            }
        };
    }

    public abstract void completed(HttpResponse httpResponse);

    public void failed(Exception e)
    {
        LOGGER.error("http请求[{}]发生异常. e:{}", url, e);
    }

    public void cancelled()
    {
        LOGGER.warn("http请求[{}]被取消.", url);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Header[] getHeaders()
    {
        return headers;
    }

    public void setHeaders(Header[] headers)
    {
        this.headers = headers;
    }
}
