package com.banquito.transaction.request.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RSGeneric implements Serializable {
    private String message;
    private Object data;
}
