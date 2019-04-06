package com.bihang.seaya.action.param.req;

public interface SeayaRequest {

    /**
     * get request method
     * @return
     */
    String getMethod() ;

    /**
     * get request url
     * @return
     */
    String getUrl() ;

    /**
     * get cookie by key
     * @param key
     * @return return cookie by key
     */
    Cookie getCookie(String key) ;

}
