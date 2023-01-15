package com.banquito.transaction.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transaction")
public class Transaction implements Serializable{

    @Id
    private String id;

    private String type;

    private String concept;

    private String description;

    private Number value;

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
