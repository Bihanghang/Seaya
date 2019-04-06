package com.bihang.seaya.bean;

import com.bihang.seaya.reflect.ClassScanner;
import com.bihang.seaya.route.RouteProcess;

import java.util.Map;

public final class SeayaBeanManager {


    private SeayaBeanManager(){
    }

    private static volatile SeayaBeanManager seayaBeanManager;

    private static SeayaBeanFactory seayaBeanFactory ;

    public static SeayaBeanManager getInstance() {
        if (seayaBeanManager == null) {
            synchronized (RouteProcess.class) {
                if (seayaBeanManager == null) {
                    seayaBeanManager = new SeayaBeanManager();
                }
            }
        }
        return seayaBeanManager;
    }

    /**
     * init route bean factory
     * @param packageName
     * @throws Exception
     */
    public void init(String packageName) throws Exception {
        Map<String, Class<?>> cicadaAction = ClassScanner.getSeayaAction(packageName);

        Class<?> bean = ClassScanner.getCustomRouteBean();
        seayaBeanFactory = (SeayaBeanFactory) bean.newInstance() ;

        for (Map.Entry<String, Class<?>> classEntry : cicadaAction.entrySet()) {
            Object instance = classEntry.getValue().newInstance();
            seayaBeanFactory.register(instance) ;
        }

    }


    /**
     * get route bean
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) throws Exception {
        return seayaBeanFactory.getBean(name) ;
    }


    /**
     * release all beans
     */
    public void releaseBean(){
        seayaBeanFactory.releaseBean();
    }
}
