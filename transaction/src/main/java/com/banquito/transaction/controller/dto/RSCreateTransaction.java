package com.banquito.transaction.controller.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSCreateTransaction implements Serializable {
    private String codeUniqueTransaction; 
}
