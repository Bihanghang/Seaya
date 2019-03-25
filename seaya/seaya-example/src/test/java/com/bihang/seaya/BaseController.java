package com.bihang.seaya;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By bihang
 * 2019/1/17 9:37
 */
public abstract class BaseController {

    @Autowired
    private ResponseFactory responseFactory;

    public <T> DefaultResponse<T> success(T data) {
        return responseFactory.success(data);
    }
}
