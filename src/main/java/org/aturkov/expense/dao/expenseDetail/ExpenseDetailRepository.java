package org.aturkov.expense.dao.expenseDetail;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseDetailRepository extends CrudRepository<ExpenseDetailEntity, UUID> {
    @Override
    List<ExpenseDetailEntity> findAll();

    void deleteAllByIdIn(List<UUID> ids);

    @Query("SELECT ed FROM ExpenseDetailEntity ed WHERE (MONTH(ed.planPaymentDate) = :month) AND YEAR(ed.planPaymentDate) = :year AND ed.paid = false")
    List<ExpenseDetailEntity> findByPaymentDateAndNotPaidInCurrentMonth(@Param("month") int month, @Param("year") int year);
}
