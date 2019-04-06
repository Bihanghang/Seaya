package com.bihang.seaya.bean.impl;

import com.bihang.seaya.bean.SeayaBeanFactory;

public class SeayaDefaultBean implements SeayaBeanFactory {
    @Override
    public void register(Object object) {

    }

    @Override
    public Object getBean(String name) throws Exception {
        Class<?> aClass = Class.forName(name);
        return aClass.newInstance();
    }

    @Override
    public void releaseBean() {
    }
}
