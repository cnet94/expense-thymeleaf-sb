package org.aturkov.expense.dao.detail;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.OperationType;
import org.aturkov.expense.domain.PaymentPeriod;
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

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "`order`")
    private Integer order;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "payment_period_type")
    @Enumerated(EnumType.STRING)
    private PaymentPeriod period;

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

    @Column(name = "deposit_id")
    private UUID depositId;

    @Column(name = "load_amount_date")
    private Timestamp loadAmountDate;

    @ManyToOne
    @JoinColumn(name = "template_id", updatable = false, insertable = false)
    private TemplateEntity template;

    @ManyToOne
    @JoinColumn(name = "item_id", updatable = false, insertable = false)
    private ItemEntity item;

    @Transient
    private ValidityPeriod validityPeriod;

    @Transient
    private boolean autoCreatePlanPaymentDate;
}

