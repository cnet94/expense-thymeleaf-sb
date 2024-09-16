package org.aturkov.expense.service;


import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.*;
import org.aturkov.expense.domain.ValidityPeriod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseDetailService {
    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final ExpenseDetailAttachmentRepository expenseDetailAttachmentRepository;

    public ExpenseDetailEntity getExpenseDetailById(UUID id) {
        return expenseDetailRepository.findById(id).orElse(null);
    }

    public void updateExpenseDetail(ExpenseDetailEntity expenseDetailEntity) {
        expenseDetailRepository.save(expenseDetailEntity);
    }

    public List<ExpenseDetailEntity> getExpenseDetailsForCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        return expenseDetailRepository.findAllByPaymentDateInCurrentMonth(currentMonth, currentYear);
    }

    @Transactional
    public void approvePaymentExpenseDetail(UUID expenseId , UUID detailId, MultipartFile file) throws IOException {
        Optional<ExpenseEntity> expenseFromDB = expenseRepository.findById(expenseId);
        if (expenseFromDB.isEmpty())
            //todo custom handler exception
            throw new RuntimeException();
        ExpenseEntity currentExpense = expenseFromDB.get();
        ExpenseDetailEntity currentExpenseDetail = currentExpense.getDetails().stream().filter(e -> e.getId().equals(detailId)).findFirst().get();

        expenseDetailApprovePayment(currentExpenseDetail);
        if (file != null && !file.isEmpty())
            expenseDetailSaveAttachment(detailId ,file);

        boolean checkExistsType = expenseService.checkExistsType(currentExpense, List.of(ExpenseEntity.Type.PAYMENT_RECURRING, ExpenseEntity.Type.BUDGET));
        boolean checkExpiryDate = expenseService.checkExpiryDate(currentExpense);
        if (checkExistsType && checkExpiryDate)
            getNewExpenseDetail(currentExpense);
    }

    public void getNewExpenseDetail(ExpenseEntity expense) {
        ExpenseDetailEntity expenseDetail = new ExpenseDetailEntity()
                .setExpenseId(expense.getId())
                .setName(expense.getName())
                .setAmount(expense.getAmount())
                .setCurrency(expense.getCurrency())
                .setPlanPaymentDate(paymentDate(expense))
                .setPeriod(expense.getPeriod())
                .setPaid(false);
        expenseDetailRepository.save(expenseDetail);
    }

    public void expenseDetailDelete(UUID detailId) {
        expenseDetailRepository.deleteById(detailId);
    }

    private Timestamp paymentDate(ExpenseEntity expense){
        //todo сделать рассчет с учетом выходных дней
        LocalDate localDate = LocalDate.now().plusMonths(1).withDayOfMonth(expense.getPaymentDay());
        LocalDateTime localDateTime = localDate.atStartOfDay();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Timestamp.from(instant);
    }

    public ValidityPeriod calculatePeriod(LocalDateTime paymentDate, int offset) {
        YearMonth targetMonth = YearMonth.from(paymentDate).plusMonths(offset);
        LocalDateTime startOfPeriod = targetMonth.atDay(1).atTime(00, 00, 00);
        LocalDateTime endOfPeriod = targetMonth.atEndOfMonth().atTime(23, 59, 59);
        return new ValidityPeriod().setFrom(startOfPeriod).setTo(endOfPeriod);
    }

    private void expenseDetailApprovePayment(ExpenseDetailEntity expenseDetail) {
        expenseDetail.setPaid(true);
        expenseDetail.setFactPaymentDate(Timestamp.from(Instant.now()));
        expenseDetailRepository.save(expenseDetail);
    }

    public void expenseDetailCancelPayment(UUID id) {
        ExpenseDetailEntity expenseDetail = expenseDetailRepository.findById(id).get();
        expenseDetail.setPaid(false);
        expenseDetail.setFactPaymentDate(null);
        expenseDetailRepository.save(expenseDetail);
    }

    private void expenseDetailSaveAttachment(UUID detailId, MultipartFile file) throws IOException {
        String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/uploads";
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = java.nio.file.Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        //todo any users different path
        expenseDetailAttachmentRepository.save(new ExpenseDetailAttachmentEntity()
                .setExpenseDetailId(detailId)
                .setStorageLink(fileNameAndPath.toString()));
    }
}
