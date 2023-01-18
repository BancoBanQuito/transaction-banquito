package com.banquito.transaction.Utils;

public enum Status {

    SUCCESSFUL("SUC", "EXITOSO"),
    PENDING("PEN", "PENDIENTE"),
    DECLINED("DEC", "RECHAZADA");

    public final String code; 
    public final String name;
    
    Status(String code, String name){
        this.code = code; 
        this.name = name; 
    }

}
