package handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import message.BinaryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
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

    /**
     * 初始化消息集合
     * @param path 消息协议配置文件
     */
    public void init(String path) throws Exception
    {
        File file = new File(path);
        if (!file.isFile())
        {
            throw new FileNotFoundException("配置文件不存在. File: " + path);
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(file);
        Element element = document.getDocumentElement();

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node item = childNodes.item(i);
            if ("msg".equalsIgnoreCase(item.getNodeName()))
            {
                NamedNodeMap attributes = item.getAttributes();
                String messageName = attributes.getNamedItem("message").getNodeValue().trim();
                Class<?> message = Class.forName(messageName);
                if (message == null)
                {
                    throw new UnsupportedOperationException("定义的message不存在.message:" + messageName);
                }
                int msgId = -1;
                if (BinaryMessage.class.isAssignableFrom(message))
                {
                    msgId = message.getField("MsgId").getInt(message);
                }
                else if (Message.class.isAssignableFrom(message))
                {
                    // 暂时使用消息名hash值作为消息id
                    // todo 后续进行优化 考虑其他方式，尽量不使用字符串
                    msgId = messageName.hashCode();
                }
                else
                {
                    throw new UnsupportedOperationException("定义了不支持的类型message, message:" + messageName);
                }

                String handlerName = attributes.getNamedItem("handler").getNodeValue().trim();
                if (msgId == -1 || handlerMap.containsKey(msgId))
                {
                    throw new UnsupportedOperationException("存在重复的消息id映射.message:" + messageName + " handler:" + handlerName);
                }

                Class<? extends Handler> handler = (Class<? extends Handler>) Class.forName(handlerName);
                if (handler == null)
                {
                    throw new UnsupportedOperationException("handler不存在,请检查. handler:" + handlerName);
                }

                handlerMap.put(msgId, new HandlerElement(message, handler));
                LOGGER.info("消息ID:[{}] message:[{}] handler:[{}] 注册.", msgId, messageName, handlerName);
            }
        }
    }

    /**
     * 解析消息的到handler
     * @param msgId
     * @param data
     * @return
     */
    public Handler parseHandler(int msgId, ByteBuf data)
    {
        HandlerElement handlerElement = handlerMap.get(msgId);
        if (handlerElement == null)
        {
            LOGGER.error("消息ID:[{}]对应的handler不存在.", msgId);
            return null;
        }

        try
        {
            Handler handler = handlerElement.createHandler();
            Class message = handlerElement.getMessage();
            if (BinaryMessage.class.isAssignableFrom(message))
            {
                Method method = message.getDeclaredMethod("readBy", ByteBuf.class);
                Object msg = method.invoke(null, data.readBytes(data.readableBytes()));
                handler.setMsg(msg);
            }
            else if (Message.class.isAssignableFrom(message))
            {
                Method method = message.getDeclaredMethod("parseFrom", byte[].class);
                byte[] array = new byte[data.readableBytes()];
                data.readBytes(array);
                Object msg = method.invoke(null, array);
                handler.setMsg(msg);
            }

            return handler;
        }
        catch (Exception e)
        {
            LOGGER.info("解析handler发生异常. msgId:{}", msgId, e);
        }

        return null;
    }

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
