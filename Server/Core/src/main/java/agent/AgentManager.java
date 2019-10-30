package agent;

import common.agent.agent.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by YongQc
 *
 * 2019-10-30 14:36.
 *
 * AgentManager
 *
 * 热更管理器
 */
public class AgentManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentManager.class);
    /** 版本号文件 */
    private static final String VERSION_FILE = "version";

    public static void init() throws Exception
    {
        File file = new File(VERSION_FILE);

        String version = "default";
        if (file.exists())
        {
            try(FileInputStream stream = new FileInputStream(file))
            {
                byte[] line = new byte[stream.available()];
                stream.read(line);
                version = new String(line);
            }
        }

        LOGGER.info("gameServer version is {}.", version);
        doAgentFile(Agent.getClassPath(), version);
        doAgentFile(Agent.getJavaPath(), version);

        // 创建新版本文件
        String name = Agent.getJavaPath() + version;
        new File(name).mkdir();
        LOGGER.info("agent create new directory. {}", name);
        Agent.addJavaPathSuffix(version);
        LOGGER.info("gameServer now agent directory is: {}", name);

        Agent.initialize();
    }

    /**
     * 处理代理文件
     * @param classPath
     * @param version
     */
    private static void doAgentFile(String classPath, String version)
    {
        // 删除agentClass下所有文件
        File dir = new File(classPath);

        if (!dir.exists())
        {
            dir.mkdirs();
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            File _file = files[i];
            if (_file.isFile())
            {
                LOGGER.info("agent delete error file. {}", _file.getName());
                _deleteFile(classPath, _file);
            }
            else if (!_file.getName().equalsIgnoreCase(version))
            {
                // 版本改变 删除
                LOGGER.info("agent delete old directory. {}", _file.getName());
                _deleteFile(classPath, _file);
            }
        }
    }

    /**
     * 删除文件
     * @param path
     * @param file
     */
    private static void _deleteFile(String path, File file)
    {
        FileUtil.deleteFile(path, file);
    }
}
