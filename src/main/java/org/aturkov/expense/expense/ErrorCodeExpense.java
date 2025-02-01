package org.aturkov.expense.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@RequiredArgsConstructor
public enum ErrorCodeExpense {
    UUID_UNKNOWN(10000, "uuid is unknown"),
    UUID_ALREADY_EXIST(10001, "uuid is already exist"),
    ENTITY_INVALID(10002, "entity invalid"),
    ENTITY_ALREADY_EXIST(10003, "entity is already exist in db. Please check unique keys"),
    USER_UNKNOWN(10101, "unknown user"),
    USER_LOCALE_UNKNOWN(10102, "unknown locale"),
    DOMAIN_UNKNOWN(10201, "unknown domain"),
    DOMAIN_TYPE_UNSUPPORTED(10202, "domain type unsupported"),
    DOMAIN_KEY_INCORRECT(10203, "domain key is incorrect"),
    DOMAIN_KEY_UNAVAILABLE(10204, "domain key is already in use"),
    DOMAIN_USER_ALREADY_EXISTS(10205, "domain user already exists"),
    DOMAIN_USER_NOT_EXISTS(10206, "domain user is not registered"),
    DOMAIN_BUSINESS_ACCOUNT_ALREADY_EXISTS(10207, "domain business_account already exists"),
    DOMAIN_BUSINESS_ACCOUNT_NOT_EXISTS(10208, "domain business_account is not registered"),
    DOMAIN_LOCALE_UNKNOWN(10209, "unknown locale"),
    DOMAIN_OR_BUSINESS_ACCOUNT_USER_NOT_EXISTS(10210, "domain or business_account user not exists"),
    PERMISSION_SCHEMA_NOT_ALLOWED(10301, "permission schema is not allowed"),
    PERMISSION_ID_UNKNOWN(10302, "permission id unknown"),
    TWIN_NOT_PROTECTED(10303, "Twin is not protected by permission"),
    PERMISSION_SCHEMA_NOT_SPECIFIED(10304, "permission schema is not specified"),
    TWIN_CLASS_SCHEMA_NOT_ALLOWED(10401, "twin class schema is not allowed"),
    TWIN_CLASS_FIELD_KEY_UNKNOWN(10402, "twin class field key is unknown"),
    TWIN_CLASS_FIELD_VALUE_TYPE_INCORRECT(10403, "twin class field value type is incorrect"),
    TWIN_CLASS_FIELD_VALUE_MULTIPLY_OPTIONS_ARE_NOT_ALLOWED(10404, "twin class field value multiply options are not allowed"),;

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
