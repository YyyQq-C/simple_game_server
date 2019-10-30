package utils;

import constant.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by YongQc
 *
 * 2019-10-30 14:53.
 *
 * FileUtil
 */
public class FileUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 删除文件
     * @param path 文件路径
     * @param file 文件
     */
    public static void deleteFile(String path, File file)
    {
        if (SystemConstant.IS_WINDOWS)
        {
            deleteFile(file);
        }
        else
        {
            path = path.replace("\\", "/");
            if (!path.endsWith("/"))
                path += "/";

            String fileName = path + file.getName();
            try
            {
                Runtime.getRuntime().exec("rm -rf " + fileName);
            }
            catch (IOException e)
            {
                LOGGER.error("delete file:{} failed.", fileName, e);
            }
        }
    }

    /**
     * 删除文件
     * @param fileName 文件路径
     */
    public static void deleteFile(String fileName)
    {
        if (fileName == null || fileName.isEmpty())
            return;

        File file = new File(fileName);
        if (!file.exists())
            return;

        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files == null)
                return;

            for (int i = 0; i < files.length; i++)
            {
                deleteFile(files[i]);
            }
        }

        file.delete();
    }

    /**
     * 删除文件
     * @param file 文件/文件夹
     */
    public static void deleteFile(File file)
    {
        if (file == null)
            return;

        if (!file.exists())
            return;

        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files == null)
                return;

            for (int i = 0; i < files.length; i++)
            {
                deleteFile(files[i]);
            }
        }

        file.delete();
    }

}
