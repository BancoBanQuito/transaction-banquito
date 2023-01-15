package com.banquito.transaction.controller.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSCreateTransaction{
    private String codeUniqueTransaction;
    
}
