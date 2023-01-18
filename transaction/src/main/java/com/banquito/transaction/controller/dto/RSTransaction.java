package com.banquito.transaction.controller.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSTransaction{

    private String codeInternationalAccount;

    private String codeLocalAccount;

    private String codeUniqueTransaction;

    private String movement;

    private String value;

    private String executeDate;

    private String presentBalance;

}
