package org.aturkov.expense.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCodeExpense {
    UUID_UNKNOWN(10000, "uuid is unknown"),
    UUID_ALREADY_EXIST(10001, "uuid is already exist"),
    ENTITY_INVALID(10002, "entity invalid"),
    ITEM_DELETE_FAILED(10101, "item[%s] cannot be deleted"),
    PAGINATION_ERROR(10201, "pagination offset must be a multiple of the size"),
    PAGINATION_LIMIT_ERROR(10202, "pagination value limit cannot be less than 1")
;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCodeExpense(int code, String message) {
        this(code, message, HttpStatus.BAD_REQUEST);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
