package com.banquito.transaction.request.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RQAccountBalance {
    private BigDecimal presentBalance;

    private BigDecimal availableBalance;
}
