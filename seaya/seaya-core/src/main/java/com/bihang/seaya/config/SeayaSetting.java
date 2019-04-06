package com.bihang.seaya.config;

import com.bihang.seaya.SeayaServer;
import com.bihang.seaya.bean.SeayaBeanManager;
import com.bihang.seaya.configuration.AbstractSeayaConfiguration;
import com.bihang.seaya.configuration.ApplicationConfiguration;
import com.bihang.seaya.configuration.ConfigurationHolder;
import com.bihang.seaya.constant.SeayaConstant;
import com.bihang.seaya.exception.SeayaException;
import com.bihang.seaya.reflect.ClassScanner;
import com.bihang.seaya.thread.ThreadLocalHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.bihang.seaya.configuration.ConfigurationHolder.getConfiguration;
import static com.bihang.seaya.constant.SeayaConstant.SystemProperties.APPLICATION_THREAD_MAIN_NAME;
import static com.bihang.seaya.constant.SeayaConstant.SystemProperties.LOGO;

public class SeayaSetting {

    /**
     * @param clazz
     * @param rootPath
     * @throws Exception
     */
    public static void setting(Class<?> clazz, String rootPath) throws Exception {

        // Seaya logo
        logo();

        //Initialize the application configuration
        initConfiguration(clazz);

        //Set application configuration
        setAppConfig(rootPath);

        //init route bean factory
        SeayaBeanManager.getInstance().init(rootPath);
    }


    private static void logo() {
        System.out.println(LOGO);
        Thread.currentThread().setName(APPLICATION_THREAD_MAIN_NAME) ;
    }


    /**
     * Set application configuration
     *
     * @param rootPath
     */
    private static void setAppConfig(String rootPath) {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfiguration(ApplicationConfiguration.class);

        if (rootPath == null) {
            rootPath = applicationConfiguration.get(SeayaConstant.ROOT_PATH);
        }
        String port = applicationConfiguration.get(SeayaConstant.SEAYA_PORT);

        if (rootPath == null) {
            throw new SeayaException("No [seaya.root.path] exists ");
        }
        if (port == null) {
            throw new SeayaException("No [seaya.port] exists ");
        }
        AppConfig.getInstance().setRootPath(rootPath);
        AppConfig.getInstance().setPort(Integer.parseInt(port));
    }


    /**
     * Initialize the application configuration
     *
     * @param clazz
     * @throws Exception
     */
    private static void initConfiguration(Class<?> clazz) throws Exception {
        ThreadLocalHolder.setLocalTime(System.currentTimeMillis());
        AppConfig.getInstance().setRootPackageName(clazz);

        //获取所有配置类，默认自带的配置类为Application
        List<Class<?>> configuration = ClassScanner.getConfiguration(AppConfig.getInstance().getRootPackageName());
        for (Class<?> aClass : configuration) {
            AbstractSeayaConfiguration conf = (AbstractSeayaConfiguration) aClass.newInstance();

            // First read
            InputStream stream ;
            //首先查看是否具有启动参数
            String systemProperty = System.getProperty(conf.getPropertiesName());
            if (systemProperty != null) {
                stream = new FileInputStream(new File(systemProperty));
            } else {
                stream = SeayaServer.class.getClassLoader().getResourceAsStream(conf.getPropertiesName());
            }

            Properties properties = new Properties();
            properties.load(stream);
            conf.setProperties(properties);

            // add configuration cache
            ConfigurationHolder.addConfiguration(aClass.getName(), conf);
        }
    }
}
