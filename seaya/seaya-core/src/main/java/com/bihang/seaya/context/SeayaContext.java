package com.bihang.seaya.context;

import com.alibaba.fastjson.JSON;
import com.bihang.seaya.action.param.req.SeayaRequest;
import com.bihang.seaya.action.param.res.SeayaResponse;
import com.bihang.seaya.constant.SeayaConstant;
import com.bihang.seaya.thread.ThreadLocalHolder;

public class SeayaContext {

    /**
     * current thread request
     */
    private SeayaRequest request ;

    /**
     * current thread response
     */
    private SeayaResponse response ;

    public SeayaContext(SeayaRequest request, SeayaResponse response) {
        this.request = request;
        this.response = response;
    }


    /**
     * response json message
     * @param workRes
     */
    public void json(Object workRes){
        SeayaContext.getResponse().setContentType(SeayaConstant.ContentType.JSON);
        SeayaContext.getResponse().setHttpContent(JSON.toJSONString(workRes));
    }

    /**
     * response text message
     * @param text response body
     */
    public void text(String text){
        SeayaContext.getResponse().setContentType(SeayaConstant.ContentType.TEXT);
        SeayaContext.getResponse().setHttpContent(text);
    }

    /**
     * response html
     * @param html response body
     */
    public void html(String html){
        SeayaContext.getResponse().setContentType(SeayaConstant.ContentType.HTML);
        SeayaContext.getResponse().setHttpContent(html);
    }

    public static SeayaRequest getRequest(){
        return SeayaContext.getContext().request ;
    }

    public SeayaRequest request(){
        return SeayaContext.getContext().request ;
    }

    public static SeayaResponse getResponse(){
        return SeayaContext.getContext().response ;
    }

    public static void setContext(SeayaContext context){
        ThreadLocalHolder.setSeayaContext(context);
    }


    public static void removeContext(){
        ThreadLocalHolder.removeSeayaContext();
    }

    public static SeayaContext getContext(){
        return ThreadLocalHolder.getSeayaContext() ;
    }
}
