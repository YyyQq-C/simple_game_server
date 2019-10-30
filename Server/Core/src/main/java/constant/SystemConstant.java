package constant;

/**
 * Created by YongQc
 *
 * 2019-10-26 15:36.
 *
 * SystemConstant
 *
 * 系统常量
 *
 */
public class SystemConstant
{
    /** 系统核心数 */
    public static final int CORE_NUM = Runtime.getRuntime().availableProcessors();
    /** 操作系统 */
    public static final boolean IS_WINDOWS;

    static
    {
        String os = System.getProperty("os.name");
        IS_WINDOWS = os.substring(0, os.length() > 3 ? 3 : os.length()).equalsIgnoreCase("win");
    }

}
