package org.aturkov.expense.dao.template;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@FieldNameConstants
@Table(name = "template")
public class TemplateEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "percent")
    private Double percent;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "payment_period_type")
    @Enumerated(EnumType.STRING)
    private ValidityPeriod.Time period;

    @Column(name = "payment_count")
    private Integer paymentCount;

    @Column(name = "payment_day")
    private Integer paymentDay;

    @Column(name = "payment_date")
    private Timestamp paymentDate;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Column(name = "weekend")
    private Boolean weekend;

    @Column(name = "depend_template_id")
    private UUID dependTemplateId;

    @Column(name = "template_period_id")
    @Enumerated(EnumType.STRING)
    private TemplatePeriod templatePeriodId;

    @Column(name = "deposit_id")
    private UUID depositId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "active")
    private Boolean active;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "depend_template_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TemplateEntity dependTemplate;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepositEntity deposit;

    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    private List<ExpenseDetailEntity> details;

    @Transient
    private Timestamp tempPaymentDate;

    @Transient
    private Balance generalBalance;

    @Transient
    private double detailAmount;

    @Transient
    private double reminder;

    @Transient
    private boolean paymentInCurrentMonth;
}
