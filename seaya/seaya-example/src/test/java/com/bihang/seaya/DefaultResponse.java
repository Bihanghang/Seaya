package com.bihang.seaya;

/**
 * Created By bihang
 * 2019/1/17 9:27
 */
public class DefaultResponse<T> extends AbstractResponse {

    public DefaultResponse(T data, int code) {
        super(data, code);
    }
}
