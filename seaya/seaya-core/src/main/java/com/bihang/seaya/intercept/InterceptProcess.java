package com.bihang.seaya.intercept;

import com.bihang.seaya.action.param.Param;
import com.bihang.seaya.config.AppConfig;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.reflect.ClassScanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InterceptProcess {

    private InterceptProcess(){}

    private volatile static InterceptProcess process ;

    private static List<SeayaInterceptor> interceptors ;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     * @return
     */
    public static InterceptProcess getInstance(){
        if (process == null){
            synchronized (InterceptProcess.class){
                if (process == null){
                    process = new InterceptProcess() ;
                }
            }
        }
        return process ;
    }


    public void loadInterceptors() throws Exception {

        if (interceptors != null){
            return;
        }else {
            interceptors = new ArrayList<>(10) ;
            Map<Class<?>, Integer> seayaInterceptor = ClassScanner.getSeayaInterceptor(appConfig.getRootPackageName());
            for (Map.Entry<Class<?>, Integer> classEntry : seayaInterceptor.entrySet()) {
                Class<?> interceptorClass = classEntry.getKey();
                SeayaInterceptor interceptor = (SeayaInterceptor) interceptorClass.newInstance();
                interceptor.setOrder(classEntry.getValue());
                interceptors.add(interceptor);
            }
            Collections.sort(interceptors,new OrderComparator());
        }
    }


    /**
     * execute before
     * @param param
     * @throws Exception
     */
    public boolean processBefore(Param param) throws Exception {
        for (SeayaInterceptor interceptor : interceptors) {
            boolean access = interceptor.before(SeayaContext.getContext(), param);
            if (!access){
                return access ;
            }
        }
        return true;
    }

    /**
     * execute after
     * @param param
     * @throws Exception
     */
    public void processAfter(Param param) throws Exception{
        for (SeayaInterceptor interceptor : interceptors) {
            interceptor.after(SeayaContext.getContext(),param) ;
        }
    }

}
