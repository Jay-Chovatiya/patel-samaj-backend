package com.patelsamaj.patelsamaj_backend.controller;

import com.patelsamaj.patelsamaj_backend.dto.DailyExpenseSummaryDTO;
import com.patelsamaj.patelsamaj_backend.dto.ExpenseDTO;
import com.patelsamaj.patelsamaj_backend.helper.SecurityUtils;
import com.patelsamaj.patelsamaj_backend.service.impl.ExpenseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpense() {
        return ResponseEntity.ok(expenseService.getAllExpense());
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable int expenseId) {
        return ResponseEntity.ok(expenseService.getExpenseById(expenseId));
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> upsertExpense(@Valid @RequestBody ExpenseDTO dto) {
        int adminId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(expenseService.upsertExpense(dto, adminId));
    }

    @GetMapping("/daily-summary")
    public ResponseEntity<List<DailyExpenseSummaryDTO>> getDailySummary() {
        return ResponseEntity.ok(expenseService.getDailyUsage());
    }
}
