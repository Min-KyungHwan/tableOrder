package com.mkh.tableOrder.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends MASException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String msg) {
        super(msg);
        super.httpStatus = HttpStatus.UNAUTHORIZED;
    }
}
