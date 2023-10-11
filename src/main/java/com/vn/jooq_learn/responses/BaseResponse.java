package com.vn.jooq_learn.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private String statusCode;
    private String message;
    private Object data;
}
