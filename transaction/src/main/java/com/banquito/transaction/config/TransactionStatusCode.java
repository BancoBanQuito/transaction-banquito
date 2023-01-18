package com.banquito.transaction.config;

public enum TransactionStatusCode {

    SUCCESFUL("SUC", "EXITOSO"), 
    PENDING("PEN", "PENDIENTE"),
    DECLINED("DEC", "RECHAZADA");

    public final String code; 
    public final String name;
    
    TransactionStatusCode(String code, String name){
        this.code = code; 
        this.name = name; 
    }

}
