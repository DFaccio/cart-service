package com.cart_service.util.exceptions;

import com.cart_service.util.MessageUtil;

public class ValidationsException extends Exception {

    public ValidationsException(String code) {
        super(MessageUtil.getMessage(code));
    }

    public ValidationsException(String code, String... args) {
        super(MessageUtil.getMessage(code, args));
    }
}