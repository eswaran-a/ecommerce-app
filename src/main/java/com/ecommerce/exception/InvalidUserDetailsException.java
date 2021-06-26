package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Password length should be greater than 7 and match the Confirm Password")
public class InvalidUserDetailsException extends RuntimeException {

    public InvalidUserDetailsException() {
    }
}
