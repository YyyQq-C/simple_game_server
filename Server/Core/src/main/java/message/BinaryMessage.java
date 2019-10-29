package message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YongQc
 *
 * 2019-10-29 14:05.
 *
 * BinaryMessage
 */
public abstract class BinaryMessage extends BaseBinaryMessage
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryMessage.class);
    public abstract int getMsgId();
}
