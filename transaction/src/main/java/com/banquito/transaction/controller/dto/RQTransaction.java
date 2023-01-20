package com.banquito.transaction.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RQTransaction implements Serializable{

    private String movement;

    private String type;

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String concept;

    private String description;

    private BigDecimal value;

    private String recipientAccountNumber;

    private String recipientBank;

    private String recipientType;
}
