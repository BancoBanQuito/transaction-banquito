package com.banquito.transaction.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Document(collection = "transaction")
public class Transaction {

    @Id
    private String id;

    private String movement;

    private String type;

    private String concept;

    private String description;

    private BigDecimal value;

    private String status;

    private String codeInternationalAccount;

    private String codeLocalAccount;

    private String codeUniqueTransaction;

    private String createDate;

    private String executeDate;

    private String recipientAccountNumber;

    private String recipientBank;

    private String recipientType;

    @Version
    private Long version;

}
