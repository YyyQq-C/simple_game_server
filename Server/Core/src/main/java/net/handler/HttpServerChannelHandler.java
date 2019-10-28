package net.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by YongQc
 *
 * 2019-10-28 09:54.
 *
 * HttpServerChannelHandler
 */
public abstract class HttpServerChannelHandler extends BaseChannelInHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerChannelHandler.class);

    private static final String CONTENT_TYPE_JSON = "application/json";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if (ctx == null || msg == null)
            return;

        if (!(msg instanceof FullHttpRequest))
        {
            ctx.close();
            return;
        }

        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        // chrome等浏览器会请求一次.ico
        if (uri.endsWith(".ico"))
        {
            _response(ctx, "");
            return;
        }
        String res = "";
        try
        {
            // 请求参数转为Map存储
            Map<String, Object> params = new HashMap<>();
            // 先解析url上的参数
            QueryStringDecoder decoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);
            Map<String, List<String>> map = decoder.parameters();
            Iterator<Map.Entry<String, List<String>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, List<String>> next = iterator.next();
                if (next.getValue().isEmpty())
                    params.put(next.getKey(), null);
                else
                    params.put(next.getKey(), next.getValue().get(0));
            }

            HttpMethod method = request.method();
            HttpHeaders headers = request.headers();
            // 处理POST请求
            if (HttpMethod.POST == method)
            {
                ByteBuf content = request.content();
                String content_type = headers.get(HttpHeaderNames.CONTENT_TYPE);
                // 处理内容为JSON格式的
                if (CONTENT_TYPE_JSON.equalsIgnoreCase(content_type))
                {
                    String requestContent = content.toString(CharsetUtil.UTF_8);
                    JSONObject obj = JSON.parseObject(requestContent);
                    if (obj != null)
                    {
                        Set<String> keySet = obj.keySet();
                        keySet.forEach(key -> params.put(key, obj.get(key)));
                    }
                }
                else
                {
                    HttpPostRequestDecoder dec = new HttpPostRequestDecoder(request);
                    try
                    {
                        while (dec.hasNext())
                        {
                            InterfaceHttpData data = dec.next();
                            if (data instanceof Attribute)
                            {
                                params.put(data.getName(), ((Attribute) data).getValue());
                            }
                        }
                    }
                    finally
                    {
                        dec.destroy();
                    }
                }
            }
            else if (HttpMethod.GET != method)
            {
                LOGGER.error("不支持的HTTP请求类型. method:{}", method);
                ctx.close();
                return;
            }

            res = dispose(ctx.channel().remoteAddress().toString(), headers, params);
        }
        catch (Exception e)
        {
            res = e.getMessage();
            LOGGER.error("处理Http请求发生异常.e:{}", e);
        }

        _response(ctx, res);
    }

    /**
     * 处理请求
     *
     * @param ip 来源
     * @param headers 请求头
     * @param param 请求参数
     * @return 返回结果
     */
    public abstract String dispose(String ip, HttpHeaders headers, Map<String, Object> param);

    private void _response(ChannelHandlerContext ctx, String res)
    {
        _response(ctx, HttpResponseStatus.OK, res);
    }

    /**
     * 响应
     *
     * @param ctx
     * @param responseStatus 状态码
     * @param res 返回结果
     */
    private void _response(ChannelHandlerContext ctx, HttpResponseStatus responseStatus, String res)
    {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(res.getBytes());

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus, byteBuf);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html");
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, res.length());
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
        super.channelReadComplete(ctx);
    }
}
