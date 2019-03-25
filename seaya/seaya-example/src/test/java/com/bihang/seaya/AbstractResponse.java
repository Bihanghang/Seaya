package com.bihang.seaya;

import lombok.Data;

/**
 * Created By bihang
 * 2019/1/17 9:25
 */
@Data
public class AbstractResponse<T> implements Response2 {

    private T data;

    private int code = 0;

    public AbstractResponse(T data, int code) {
        this.data = data;
        this.code = code;
    }
}
