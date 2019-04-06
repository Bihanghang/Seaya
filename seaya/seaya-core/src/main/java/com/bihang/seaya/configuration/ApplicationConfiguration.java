package com.bihang.seaya.configuration;

import com.bihang.seaya.constant.SeayaConstant;

public class ApplicationConfiguration extends AbstractSeayaConfiguration {

    public ApplicationConfiguration() {
        super.setPropertiesName(SeayaConstant.SystemProperties.APPLICATION_PROPERTIES);
    }

}
