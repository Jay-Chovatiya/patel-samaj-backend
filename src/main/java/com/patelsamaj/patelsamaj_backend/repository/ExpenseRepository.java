package com.patelsamaj.patelsamaj_backend.repository;

import com.patelsamaj.patelsamaj_backend.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query("""
        SELECT e
        FROM Expense e
        WHERE (:search IS NULL
               OR TRIM(:search) = ''
               OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%')))
        """)
    Page<Expense> searchExpenses(@Param("search") String search, Pageable pageable);
}
