package com.erika.minicasino.common;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Standard API response wrapper")
public class BaseResponse<T>  implements Serializable{
    @Schema(description = "Application-level response code", example = "200")
    private int code;
    @Schema(description = "Response data payload")
    private T data;
    @Schema(description = "Response message", example = "ok")
    private String message;
    @Schema(description = "Detailed message", example = "Created Successful.")
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

}
