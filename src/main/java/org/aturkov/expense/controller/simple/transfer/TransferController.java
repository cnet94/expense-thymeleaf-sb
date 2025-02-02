package org.aturkov.expense.controller.simple.transfer;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.transfer.DepositTransferCreateRqDTOV1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class TransferController {

    @GetMapping("/transfer/create")
    public String showTransferDepositAddFrom(
            Model model) {
        DepositTransferCreateRqDTOV1 transfer = new DepositTransferCreateRqDTOV1();
        try {
            model.addAttribute("transfer", transfer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/deposit/transfer";
    }

}
