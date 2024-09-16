package org.aturkov.expense.controller.rest;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.ExpenseEntity;
import org.aturkov.expense.dto.ExpenseDTOv1;
import org.aturkov.expense.mapper.expense.ExpenseDTOMapper;
import org.aturkov.expense.service.ExpenseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ExpenseRestController {
    private final ExpenseService expenseService;
    private final ExpenseDTOMapper expenseDTOMapper;

    @GetMapping(value = "/private/expense/list/v1")
    public List<ExpenseDTOv1> expenseList() {
        List<ExpenseDTOv1> expenseDTOv1List = new ArrayList<>();
        for (ExpenseEntity expenseEntity : expenseService.getAll()) {
            expenseDTOv1List.add(expenseDTOMapper.map(expenseEntity));
        }
        return expenseDTOv1List;
    }
}
