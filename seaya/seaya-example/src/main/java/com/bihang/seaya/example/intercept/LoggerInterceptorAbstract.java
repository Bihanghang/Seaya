package com.bihang.seaya.example.intercept;

import com.bihang.seaya.Seaya;
import com.bihang.seaya.action.param.Param;
import com.bihang.seaya.annotation.Interceptor;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.intercept.SeayaInterceptor;
import com.bihang.seaya.log.SeayaLog;
import org.slf4j.Logger;

@Interceptor(order = 1)
public class LoggerInterceptorAbstract extends SeayaInterceptor {

    @Override
    public boolean before(SeayaContext context, Param param) throws Exception {
        SeayaLog.log("我是"+this.getOrder()+"号拦截器");
        return super.before(context, param);
    }

    @Override
    public void after(SeayaContext context, Param param) {
        SeayaLog.log("我是"+this.getOrder()+"号拦截器");
        SeayaLog.log("logger param=["+param.toString()+"]");
    }
}
