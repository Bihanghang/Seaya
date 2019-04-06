package com.bihang.seaya.thread;

import com.bihang.seaya.context.SeayaContext;
import io.netty.util.concurrent.FastThreadLocal;

public class ThreadLocalHolder {

    private static final FastThreadLocal<Long> LOCAL_TIME= new FastThreadLocal() ;

    private static final FastThreadLocal<SeayaContext> SEAYA_CONTEXT= new FastThreadLocal() ;


    /**
     * 设置seaya上下文
     * @param context current context
     */
    public static void setSeayaContext(SeayaContext context){
        SEAYA_CONTEXT.set(context) ;
    }

    /**
     * 删除seaya上下文
     */
    public static void removeSeayaContext(){
        SEAYA_CONTEXT.remove();
    }

    /**
     * @return get seaya context
     */
    public static SeayaContext getSeayaContext(){
        return SEAYA_CONTEXT.get() ;
    }

    /**
     * Set time
     * @param time current time
     */
    public static void setLocalTime(long time){
        LOCAL_TIME.set(time) ;
    }

    /**
     * Get time and remove value
     * @return get local time
     */
    public static Long getLocalTime(){
        Long time = LOCAL_TIME.get();
        LOCAL_TIME.remove();
        return time;
    }

}
