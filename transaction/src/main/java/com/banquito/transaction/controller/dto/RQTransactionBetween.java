package com.banquito.transaction.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RQTransactionBetween implements Serializable {

    LocalDateTime from;

    LocalDateTime to;
}
