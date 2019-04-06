package com.bihang.seaya.action.param.res;

public class WorkRes<T> {
    private String code;

    private String message;

    private T dataBody ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDataBody() {
        return dataBody;
    }

    public void setDataBody(T dataBody) {
        this.dataBody = dataBody;
    }

    @Override
    public String toString() {
        return "WorkRes{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", dataBody=" + dataBody +
                '}';
    }
}
