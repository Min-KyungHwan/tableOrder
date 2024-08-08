package com.mkh.tableOrder.common.exception;

import org.springframework.http.HttpStatus;

public abstract class MASException extends RuntimeException {

    protected HttpStatus httpStatus;
    protected Object data;

    private static final long serialVersionUID = 1L;

    public MASException(String msg) {
        super(msg);
    }
    
    public MASException(String msg, Object data) {
        super(msg);
        this.data = data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Object getData() {
        return data;
    }

}