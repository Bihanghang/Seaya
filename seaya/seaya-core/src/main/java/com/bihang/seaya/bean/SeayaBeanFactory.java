package com.bihang.seaya.bean;

public interface SeayaBeanFactory {

    /**
     * Register into bean Factory
     * @param object
     */
    void register(Object object);

    /**
     * Get bean from bean Factory
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name) throws Exception;

    /**
     * release all beans
     */
    void releaseBean() ;

}
