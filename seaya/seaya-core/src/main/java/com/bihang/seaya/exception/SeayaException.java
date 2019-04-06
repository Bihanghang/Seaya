package com.bihang.seaya.exception;

import com.bihang.seaya.enums.StatusEnum;

public class SeayaException extends RuntimeException {

    String errorCode;
    String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SeayaException(String message) {
        super(message);
    }
    public SeayaException(StatusEnum statusEnum){
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }
    public SeayaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeayaException(Throwable cause) {
        super(cause);
    }

    public SeayaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SeayaException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }
}
