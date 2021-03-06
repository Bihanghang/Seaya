package com.bihang.seaya.configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationHolder {
    private static Map<String,AbstractSeayaConfiguration> config = new HashMap<>(8) ;

    /**
     * Add holder cache
     * @param key
     * @param configuration
     */
    public static void addConfiguration(String key,AbstractSeayaConfiguration configuration){
        config.put(key, configuration);
    }


    /**
     * Get class from cache by class name
     * @param clazz
     * @return
     */
    public static AbstractSeayaConfiguration getConfiguration(Class<? extends AbstractSeayaConfiguration> clazz){
        return config.get(clazz.getName()) ;
    }
}
