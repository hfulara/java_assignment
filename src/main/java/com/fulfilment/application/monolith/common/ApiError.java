package com.fulfilment.application.monolith.common;

import java.time.LocalDateTime;

public class ApiError {

    public LocalDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public String path;

    public ApiError(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
