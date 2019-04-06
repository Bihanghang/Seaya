package com.bihang.seaya.example.configuration;


import com.bihang.seaya.configuration.AbstractSeayaConfiguration;

public class KafkaConfiguration extends AbstractSeayaConfiguration {

    public KafkaConfiguration() {
        super.setPropertiesName("kafka.properties");
    }


}
