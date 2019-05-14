package com.bihang.seaya.action.param.res.impl;

import com.bihang.seaya.action.param.req.Cookie;
import com.bihang.seaya.action.param.res.SeayaResponse;
import com.bihang.seaya.constant.SeayaConstant;
import com.bihang.seaya.exception.SeayaException;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeayaHttpResponse implements SeayaResponse {
    private Map<String, String> headers = new HashMap<>(8);

    private String contentType;

    private String httpContent;

    private List<io.netty.handler.codec.http.cookie.Cookie> cookies = new ArrayList<>(6);

    private SeayaHttpResponse() {
    }

    public static SeayaHttpResponse init() {
        SeayaHttpResponse response = new SeayaHttpResponse();
        response.contentType = SeayaConstant.ContentType.TEXT;
        return response;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setHttpContent(String content) {
        httpContent = content;
    }

    @Override
    public String getHttpContent() {
        return this.httpContent == null ? "" : this.httpContent;
    }


    public void setHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }


    @Override
    public void setCookie(Cookie seayaCookie) {
        if (null == seayaCookie){
            throw new SeayaException("cookie is null!") ;
        }

        if (null == seayaCookie.getName()){
            throw new SeayaException("cookie.getName() is null!") ;
        }
        if (null == seayaCookie.getValue()){
            throw new SeayaException("cookie.getValue() is null!") ;
        }

        DefaultCookie cookie = new DefaultCookie(seayaCookie.getName(), seayaCookie.getValue());

        cookie.setPath("/");
        cookie.setMaxAge(seayaCookie.getMaxAge());
        //此处domain不能随意添加，会导致cookie无法写入浏览器
        /*cookie.setDomain("http://127.0.0.1");*/
        cookies.add(cookie) ;
    }

    @Override
    public List<io.netty.handler.codec.http.cookie.Cookie> cookies() {
        return cookies;
    }



}
