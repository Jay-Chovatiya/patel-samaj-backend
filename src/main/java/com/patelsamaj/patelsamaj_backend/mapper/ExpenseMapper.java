package com.patelsamaj.patelsamaj_backend.mapper;

import com.patelsamaj.patelsamaj_backend.dto.ExpenseDTO;
import com.patelsamaj.patelsamaj_backend.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ExpenseMapper extends BaseMapper<ExpenseDTO, Expense> {
    @Override
    @Mapping(target = "expenseId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(ExpenseDTO dto, @MappingTarget Expense entity);
}
