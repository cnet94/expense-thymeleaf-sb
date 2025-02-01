package org.aturkov.expense.dao.expenseDetail;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dao.historyTransaction.HistoryTransactionEntity;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.ValidityPeriod;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@FieldNameConstants
@Table(name = "expense_detail")
public class ExpenseDetailEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "`order`")
    private Integer order;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "payment_period_type")
    @Enumerated(EnumType.STRING)
    private ValidityPeriod.Time period;

    @Column(name = "plan_payment_date")
    private Timestamp planPaymentDate;

    @Column(name = "fact_payment_date")
    private Timestamp factPaymentDate;

    @Column(name = "period_date_from")
    private Timestamp periodDateFrom;

    @Column(name = "period_date_to")
    private Timestamp periodDateTo;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "depend_detail_id")
    private UUID dependDetailId;

    @ManyToOne
    @JoinColumn(name = "template_id", updatable = false, insertable = false)
    private TemplateEntity template;

//    @OneToOne
//    @
//    private HistoryTransactionEntity transaction;

    @Transient
    private ValidityPeriod validityPeriod;
}
