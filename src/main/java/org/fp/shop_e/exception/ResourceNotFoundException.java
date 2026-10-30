package org.fp.shop_e.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND, List.of(message));
    }
}

