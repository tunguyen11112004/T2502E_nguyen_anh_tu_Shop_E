package org.fp.shop_e.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BusinessException extends RuntimeException {
    private final String code;
    private final HttpStatus status;
    private final List<String> details;

    public BusinessException(String code, String message, HttpStatus status, List<String> details) {
        super(message);
        this.code = code;
        this.status = status;
        this.details = details == null ? List.of() : details;
    }

    public BusinessException(String code, String message, HttpStatus status) {
        this(code, message, status, List.of(message));
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getDetails() {
        return details;
    }
}

