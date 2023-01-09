package com.banquito.transaction.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Document(collection = "transaction")
public class Transaction {

    @Id
    private String id;

    @Field(value = "code_local_account")
    private String codeLocalAccount;

    @Field(value = "code_international_account")
    private String codeInternationalAccount;

    @Field(value = "code_unique_transaction")
    private String codeUniqueTransaction;

    @Field(value = "type")
    private String type;

    @Field(value = "recipient_account_number")
    private String recipientAccountNumber;

    @Field(value = "recipient_type")
    private String recipientType;

    @Field(value = "recipient_bank")
    private String recipientBank;

    @Field(value = "concept")
    private String concept;

    @Field(value = "description")
    private String description;

    @Field(value = "value")
    private Number value;

    @Field(value = "create_date")
    private Timestamp createDate;

    @Field(value = "execute_date")
    private Timestamp executeDate;

    @Field(value = "status")
    private String status;

}
