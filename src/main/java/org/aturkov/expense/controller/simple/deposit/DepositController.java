package org.aturkov.expense.controller.simple.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dto.deposit.*;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.mapper.deposit.DepositCreateDTOReverseMapper;
import org.aturkov.expense.mapper.deposit.DepositDTOMapper;
import org.aturkov.expense.mapper.deposit.DepositUpdateRqDTOMapper;
import org.aturkov.expense.mapper.deposit.DepositUpdateRqDTOReverseMapper;
import org.aturkov.expense.service.deposit.DepositService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DepositController {
    private final DepositDTOMapper depositDTOMapper;
    private final DepositService depositService;
    private final DepositCreateDTOReverseMapper depositCreateDTOReverseMapper;
    private final DepositUpdateRqDTOReverseMapper depositUpdateRqDTOReverseMapper;
    private final DepositUpdateRqDTOMapper depositUpdateRqDTOMapper;

    @GetMapping("/deposit/list")
    public String showDepositList(Model model) {
        List<DepositDTOv1> list;
        try {
            list = depositDTOMapper.convertCollection(depositService.findDeposits());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("depositList", list);
        return "/deposit/list";
    }

    @GetMapping("/deposit/create")
    public String showDepositAddForm(
            Model model) {
        DepositCreateRqDTOv1 rq = new DepositCreateRqDTOv1();
        rq
                .setDeposit((DepositCreateDTOv1) new DepositCreateDTOv1().setAmount(0.0));
        model.addAttribute("deposit", rq);
        return "/deposit/create-form";
    }

    @PostMapping("/deposit/add")
    public String createDeposit(
            @ModelAttribute("deposit") DepositCreateRqDTOv1 request) {
        DepositCreateRsDTOv1 rs = new DepositCreateRsDTOv1();
        try {
            DepositEntity deposit = depositService.createDeposit(depositCreateDTOReverseMapper.convert(request.getDeposit()));
            rs
                    .setDeposit(depositDTOMapper.convert(deposit));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/deposit/list";
    }

    @GetMapping("/deposit/update/{depositId}")
    public String showDepositUpdateForm(
            @PathVariable("depositId") UUID depositId,
            Model model) {
        try {
            DepositUpdateRqDTOv1 deposit = depositUpdateRqDTOMapper.convert(depositService.findDeposit(depositId));
            model.addAttribute("deposit", deposit);
        } catch (ServiceException e) {
            model.addAttribute("errorMessage", "Error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/deposit/update-form";
    }

    @PostMapping("/deposit/update/{depositId}")
    public String updateDeposit(
            @PathVariable("depositId") UUID depositId,
            DepositUpdateRqDTOv1 request) {
        DepositUpdateRsDTOv1 rs = new DepositUpdateRsDTOv1();
        try {
            DepositEntity deposit = depositService.updateDeposit(depositUpdateRqDTOReverseMapper.convert(request).setId(depositId));
            rs
                    .setDeposit(depositDTOMapper.convert(deposit));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/deposit/list";
    }

    @PostMapping("/deposit/delete/{depositId}")
    public String deleteDeposit(
            @PathVariable("depositId") UUID depositId) {
        depositService.deleteDeposit(depositId);
        return "redirect:/deposit/list";
    }

    @PostMapping("/deposit/archive/{depositId}")
    public String archiveTemplate(@PathVariable("depositId") UUID depositId) {
        try {
            depositService.changeStatus(depositId);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/deposit/list";
    }
}
