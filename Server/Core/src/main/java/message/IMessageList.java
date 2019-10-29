package message;

import java.io.Serializable;

/**
 * Created by YongQiancheng on 2017/11/7.
 * <p>
 * IMessageList
 */
public interface IMessageList extends IMessageWriteAndRead, Serializable
{
    int size();
}
