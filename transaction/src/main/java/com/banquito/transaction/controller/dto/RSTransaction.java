package com.banquito.transaction.controller.dto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RSTransaction{

    private String codeUniqueTransaction;

    private String movement;

    private String codeLocalAccount;

    private String concept;

    private LocalDateTime executeDate;

    private BigDecimal value;

    private BigDecimal presentBalance;

    private BigDecimal availableBalance;

}
