package com.mkh.tableOrder.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends MASException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String msg) {
        super(msg);
        super.httpStatus = HttpStatus.BAD_REQUEST;
    }

}
