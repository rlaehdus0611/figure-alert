package com.figure.figure.dto;

public class ErrorResponse {

    private int status;
    private String message;
    private String code;

    public ErrorResponse(int status, String message) {
        this(status, "REQUEST_FAILED", message);
    }

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
