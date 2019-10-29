package handler;

import com.google.protobuf.Message;
import message.BinaryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
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
                int msgId = -1;
                if (message.isInstance(BinaryMessage.class))
                {
                    msgId = message.getField("MsgId").getInt(message);
                }
                else if (message.isInstance(Message.class))
                {
                    msgId = (int) message.getMethod("hashCode").invoke(message);
                }

                String handlerName = attributes.getNamedItem("handler").getNodeValue().trim();
                if (msgId == -1 || handlerMap.containsKey(msgId))
                {
                    throw new UnsupportedOperationException("存在重复的消息id映射.message:" + messageName + " handler:" + handlerName);
                }

                Class<? extends Handler> handler = (Class<? extends Handler>) Class.forName(handlerName);
                handlerMap.put(msgId, new HandlerElement(message, handler));
            }
        }

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
