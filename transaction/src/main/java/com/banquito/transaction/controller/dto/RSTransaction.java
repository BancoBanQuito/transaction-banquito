package com.banquito.transaction.controller.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSTransaction implements Serializable {

    private String codeUniqueTransaction;

    private String movement;

    private String codeLocalAccount;

    private String concept;

    private LocalDateTime executeDate;

    private BigDecimal value;

    private BigDecimal presentBalance;

    private BigDecimal availableBalance;

}
