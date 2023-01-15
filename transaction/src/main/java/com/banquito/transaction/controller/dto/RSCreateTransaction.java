package com.banquito.transaction.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSCreateTransaction implements Serializable {
    private String movement; 
    private String type; 
    private String concept; 
    private BigDecimal value; 
    private String codeLocalAccount;
    private String codeUniqueTransaction;
    private String executeDate; 
    private String recipientAccountNumber; 
    
}
