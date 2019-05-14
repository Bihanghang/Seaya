package com.bihang.seaya.action.param.req.impl;

import com.bihang.seaya.action.param.req.Cookie;
import com.bihang.seaya.action.param.req.SeayaRequest;
import com.bihang.seaya.constant.SeayaConstant;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.util.HashMap;
import java.util.Map;

public class SeayaHttpRequest implements SeayaRequest {
    private String method ;

    private String url ;

    private String clientAddress ;

    private Map<String,Cookie> cookie = new HashMap<>(8) ;
    private Map<String,String> headers = new HashMap<>(8) ;

    private SeayaHttpRequest(){}

    public static SeayaHttpRequest init(DefaultHttpRequest httpRequest){
        SeayaHttpRequest request = new SeayaHttpRequest() ;
        request.method = httpRequest.method().name();
        request.url = httpRequest.uri();

        //build headers
        buildHeaders(httpRequest, request);

        //init cookies
        initCookies(request);

        return request ;
    }


    /**
     * build headers
     * @param httpRequest io.netty.httprequest
     * @param request cicada request
     */
    private static void buildHeaders(DefaultHttpRequest httpRequest, SeayaHttpRequest request) {
        for (Map.Entry<String, String> entry : httpRequest.headers().entries()) {
            request.headers.put(entry.getKey(),entry.getValue());
        }
    }

    /**
     * init cookies
     * @param request request
     */
    private static void initCookies(SeayaHttpRequest request) {
        for (Map.Entry<String, String> entry : request.headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals(SeayaConstant.ContentType.COOKIE)){
                continue;
            }

            for (io.netty.handler.codec.http.cookie.Cookie cookie : ServerCookieDecoder.LAX.decode(value)) {
                Cookie seayaCookie = new Cookie() ;
                seayaCookie.setName(cookie.name());
                seayaCookie.setValue(cookie.value());
                seayaCookie.setDomain(cookie.domain());
                seayaCookie.setMaxAge(cookie.maxAge());
                seayaCookie.setPath(cookie.path()) ;
                request.cookie.put(seayaCookie.getName(),seayaCookie) ;
            }
        }
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Cookie getCookie(String key) {
        return cookie.get(key) ;
    }
}
