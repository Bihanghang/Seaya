package com.bihang.seaya;

/**
 * Created By bihang
 * 2019/1/17 9:38
 */
public interface ResponseFactory {

    <T> DefaultResponse<T> success(T data);

}
