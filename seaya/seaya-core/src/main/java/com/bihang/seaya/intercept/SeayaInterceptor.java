package com.bihang.seaya.intercept;

import com.bihang.seaya.action.param.Param;
import com.bihang.seaya.context.SeayaContext;

public abstract class SeayaInterceptor {

    private int order ;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * before
     * @param context
     * @param param
     * @return
     * 如果过滤条件满足返回true
     * @throws Exception
     */
    protected boolean before(SeayaContext context, Param param) throws Exception{
        return true;
    }


    /**
     * after
     * @param context
     * @param param
     * @throws Exception
     */
    protected void after(SeayaContext context,Param param) throws Exception{}
}
