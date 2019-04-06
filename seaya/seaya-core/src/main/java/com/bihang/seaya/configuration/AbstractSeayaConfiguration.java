package com.bihang.seaya.configuration;

import java.util.Properties;

public class AbstractSeayaConfiguration {

    /**
     * file name
     */
    private String propertiesName;

    private Properties properties;


    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return properties.get(key) == null ? null : properties.get(key).toString();
    }

    @Override
    public String toString() {
        return "AbstractCicadaConfiguration{" +
                "propertiesName='" + propertiesName + '\'' +
                ", properties=" + properties +
                '}';
    }

}
