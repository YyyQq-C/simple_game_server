package utils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by YongQc
 *
 * 2019-10-30 15:24.
 *
 * IDGenerator
 *
 *  该生成规则能根据服务器id生成唯一的id,每秒最多生成 1<<29 个id
 *  最多生成到2082年 2083年将失效
 *
 */
public class IDGenerator
{
    private static final int START_YEAR = 2019;

    /** 种子 */
    private static final AtomicInteger SEED = new AtomicInteger(1);
    /** 种子最大值 */
    private static final int MAX_SEED = 1 << 30;

    /**
     * 得到全局唯一id
     *
     * @return
     */
    public static synchronized long getUniqueId()
    {
        return _generator();
    }

    /**
     * 生成全局唯一62位id
     *
     * 时间32位 | 种子30位
     *
     * @return
     */
    private static long _generator()
    {
        long id = _getTimeBinary();
        id = id << 30 | SEED.getAndIncrement();
        if (SEED.get() >= MAX_SEED)
            SEED.set(1);

        return id;
    }

    /**
     * 生成当前时间32位二进制
     *
     * 6位年份 | 4位月份 | 5位天 | 5位小时 | 6位分钟 | 6位秒
     *
     * @return
     */
    private static long _getTimeBinary()
    {
        Calendar calendar = Calendar.getInstance();
        // 6位最大63
        int year = calendar.get(Calendar.YEAR) - START_YEAR; // 6
        int month = calendar.get(Calendar.MONTH) + 1;  // 4
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 5
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 5
        int min = calendar.get(Calendar.MINUTE);       // 6
        int sec = calendar.get(Calendar.SECOND);       // 6
        // 时间32位
        long id = year;
        id = id << 4 | month;
        id = id << 5 | day;
        id = id << 5 | hour;
        id = id << 6 | min;
        id = id << 6 | sec;

        return id;
    }

    /**
     * 根据id获取生成时间
     *
     * @param id
     * @return
     */
    public static LocalDateTime getTimeById(long id)
    {
        id = id >> 30;
        int year = (int) (id >> 26 & 0x3f);
        int month = (int) (id >> 22 & 0xf);
        int day = (int) (id >> 17 & 0x1f);
        int hour = (int) (id >> 12 & 0x1f);
        int min = (int) (id >> 6 & 0x3f);
        int sec = (int) (id & 0x3f);

        return LocalDateTime.of(START_YEAR + year, month, day, hour, min, sec);
    }


    public static void main(String[] args)
    {

        System.out.println(getTimeById(176863088568449708L));
    }
}
