package com.bihang.seaya.example.intercept;

import com.bihang.seaya.action.param.Param;
import com.bihang.seaya.annotation.Interceptor;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.intercept.SeayaInterceptor;
import com.bihang.seaya.log.SeayaLog;


@Interceptor(order = 4)
public class ExecuteTimeInterceptor extends SeayaInterceptor {

    private Long start;

    private Long end;

    @Override
    public boolean before(SeayaContext context, Param param) {
        start = System.currentTimeMillis();
        SeayaLog.log("我是"+this.getOrder()+"号拦截器");
        SeayaLog.log("拦截请求");
        context.text("拦截请求");
        return true;
    }

    @Override
    public void after(SeayaContext context,Param param) {
        end = System.currentTimeMillis();
        SeayaLog.log("我是"+this.getOrder()+"号拦截器");

        SeayaLog.log(""+(end - start)+"");
    }
}
