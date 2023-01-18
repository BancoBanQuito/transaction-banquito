package com.banquito.transaction.Utils;

public enum RSCode {
    INTERNAL_SERVER_ERROR(500),
    NOT_FOUND(404),
    BAD_REQUEST(400),
    CREATED(201),
    SUCCESS(200);

    public final int code;
    RSCode(int code) {
        this.code = code;
    }
}