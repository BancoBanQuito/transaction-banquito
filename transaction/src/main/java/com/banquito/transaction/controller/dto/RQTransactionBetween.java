package com.banquito.transaction.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RQTransactionBetween {

    LocalDateTime from;

    LocalDateTime to;
}
