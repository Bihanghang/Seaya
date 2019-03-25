package com.bihang.seaya;

/**
 * Created By bihang
 * 2019/1/17 9:40
 */
public class AbstractResponseFactory implements ResponseFactory {

    @Override
    public <T> DefaultResponse<T> success(T data) {
        return new DefaultResponse<>(data,Response2.SUCCESS);
    }
}
