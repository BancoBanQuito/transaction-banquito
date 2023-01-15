package com.banquito.transaction.config;

public enum RSCode {
    INTERNAL_ERROR_SERVER(500),
    NOT_FOUND(404),
    INSUFFICIENT_BALANCE(400),
    CREATED(201),
    SUCCESS(200);

    public final int code;
    RSCode(int code) {
        this.code = code;
    }
}