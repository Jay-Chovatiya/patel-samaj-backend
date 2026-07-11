package com.patelsamaj.patelsamaj_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyExpenseSummaryDTO {
    private LocalDate date;
    private BigDecimal totalSpent;
}
