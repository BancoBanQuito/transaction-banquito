package com.banquito.transaction.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String type;

    private String concept;

    private String description;

    private Number value;

    private String codeInternationalAccount;

    private String codeLocalAccount;

    private String codeUniqueTransaction;

    private Timestamp createDate;

    private Timestamp executeDate;

    private String recipientAccountNumber;

    private String recipientType;

    private String recipientBank;

    private String status;

    @Version
    private Long version;
}
