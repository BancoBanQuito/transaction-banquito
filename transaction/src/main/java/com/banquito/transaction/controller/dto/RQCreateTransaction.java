package com.banquito.transaction.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RQCreateTransaction implements Serializable{
    private String type; 
    private String concept; 
    private String descripcion; 
    private BigDecimal value; 
    private String codeInternationalAccount; 
    private String codeLocalAccount;
    private String recipientAccountNumber; 
    private String recipientBank;
    private String recipientType;
}
