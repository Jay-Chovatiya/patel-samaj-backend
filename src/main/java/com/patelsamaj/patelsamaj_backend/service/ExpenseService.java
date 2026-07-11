package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.dto.DailyExpenseSummaryDTO;
import com.patelsamaj.patelsamaj_backend.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {
    ExpenseDTO getExpenseById(int expenseId);
    List<ExpenseDTO> getAllExpense();
    ExpenseDTO upsertExpense(ExpenseDTO dto, int userId);
    List<DailyExpenseSummaryDTO> getDailyUsage();
}
