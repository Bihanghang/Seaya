package com.bihang.seaya.ioc;

import com.bihang.seaya.bean.SeayaBeanFactory;
import com.bihang.seaya.log.SeayaLog;

import java.util.HashMap;
import java.util.Map;

public class SeayaIoc implements SeayaBeanFactory {

    private static Map<String,Object> beans = new HashMap<>(16) ;


    @Override
    public void register(Object object) {
        SeayaLog.log("I am SeayaIOC");
        beans.put(object.getClass().getName(),object) ;
    }

    @Override
    public Object getBean(String name) throws Exception {
        return beans.get(name);
    }

    @Override
    public void releaseBean() {
        beans = null ;
        SeayaLog.log("release all bean success.");
    }
}
