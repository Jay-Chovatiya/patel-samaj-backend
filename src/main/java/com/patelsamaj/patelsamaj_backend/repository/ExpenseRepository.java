package com.patelsamaj.patelsamaj_backend.repository;

import com.patelsamaj.patelsamaj_backend.dto.DailyExpenseSummaryDTO;
import com.patelsamaj.patelsamaj_backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("SELECT new com.patelsamaj.patelsamaj_backend.dto.DailyExpenseSummaryDTO(e.expenseDate, SUM(e.amount)) " +
            "FROM Expense e " +
            "GROUP BY e.expenseDate " +
            "ORDER BY e.expenseDate DESC")
    List<DailyExpenseSummaryDTO> getDailyUsageSummary();
}
