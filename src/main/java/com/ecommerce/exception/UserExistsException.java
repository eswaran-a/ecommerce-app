package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Username already exists")
public class UserExistsException extends RuntimeException {

    public UserExistsException() {
    }
}
