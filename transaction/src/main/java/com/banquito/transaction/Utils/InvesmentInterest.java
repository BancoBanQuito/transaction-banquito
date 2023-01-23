package com.banquito.transaction.Utils;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvesmentInterest {

    private BigDecimal rawInterest;

    private BigDecimal retention;

    private BigDecimal netInterest;
}
