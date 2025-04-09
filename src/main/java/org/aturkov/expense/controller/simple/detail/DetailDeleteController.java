package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DetailDeleteController {
    private final DetailService detailService;

    @PostMapping("/detail/{detailId}/delete/v1")
    public String deleteDetailV1(
            @PathVariable("detailId") UUID detailId,
            @RequestParam("paid") Boolean paid,
            RedirectAttributes redirectAttributes) {
        try {
            detailService.deleteExpenseDetail(detailId, paid);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/detail/list";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Message: template perhaps was deleted successfully");
        return "redirect:/detail/list";
    }

    @PostMapping("/detail/{detailId}/delete/v2")
    public String deleteDetailV2(
            @PathVariable("detailId") UUID detailId,
            @RequestParam("templateId") UUID templateId,
            @RequestParam("paid") Boolean paid,
            RedirectAttributes redirectAttributes) {
        try {
            detailService.deleteExpenseDetail(detailId, paid);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
        }
        return "redirect:/template/" + templateId + "/card";
    }
}
