package org.aturkov.expense.controller;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.ExpenseEntity;
import org.aturkov.expense.dto.ExpenseDTOv1;
import org.aturkov.expense.mapper.expense.ExpenseDetailDTOMapper;
import org.aturkov.expense.mapper.expense.ExpenseDTOMapper;
import org.aturkov.expense.service.ExpenseDetailService;
import org.aturkov.expense.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpenseDTOMapper expenseDTOMapper;

    @GetMapping("/expense/list")
    public String showExpenses(Model model) {
        List<ExpenseDTOv1> expenseList = new ArrayList<>();
        expenseService.getAll().forEach(expense -> expenseList.add(expenseDTOMapper.map(expense)));
        model.addAttribute("expenses", expenseList);
        return "/expense/list";
    }

    @GetMapping("/expense/card/{id}")
    public String getExpenseDetails(@PathVariable("id") UUID id, Model model) {
        ExpenseDTOv1 expense = expenseDTOMapper.map(expenseService.getExpenseById(id));
        model.addAttribute("expense", expense);
        return "expense/card";
    }

    @GetMapping("/expense/form-add")
    public String showAddExpenseForm(Model model) {
        model.addAttribute("expense", new ExpenseEntity());
        return "/expense/form-add";
    }

//    @PostMapping("/expense/add")
//    public String addExpense(@ModelAttribute ExpenseEntity expenseEntity) {
//        expenseService.saveExpense(expenseEntity);
//        return "redirect:/expenses";
//    }


}
