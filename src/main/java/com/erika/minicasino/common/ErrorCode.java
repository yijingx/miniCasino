package com.erika.minicasino.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Custom application error codes")
public enum ErrorCode {
    SUCCESS(200, "ok", ""),
    PARAMS_ERROR(40000, "Request parameter error", ""),
    NULL_ERROR(40001, "Request data not exists", ""),
    DUPLICATE_DATA(40100, "Data already exists", ""),
    NO_AUTH(40101, "No permission", ""),
    SYSTEM_ERROR(50000, "Internal system error", "");

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

}
