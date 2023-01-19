package com.banquito.transaction.request.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RSAccount implements Serializable {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String status;

    private String product;

    private BigDecimal presentBalance;

    private BigDecimal availableBalance;
}
