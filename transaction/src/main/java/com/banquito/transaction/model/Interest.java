package com.banquito.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interests")
public class Interest {

    @Id
    private String id;

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String codeUniqueInterest;

    private BigDecimal ear;

    private BigDecimal availableBalance;

    private BigDecimal value;

    private LocalDateTime executeDate;

    @Version
    private Long version;
}
