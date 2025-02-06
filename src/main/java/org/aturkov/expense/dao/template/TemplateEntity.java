package org.aturkov.expense.dao.template;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.Balance;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.TemplatePeriod;
import org.aturkov.expense.domain.ValidityPeriod;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Getter
    public enum OperationType {
        INCOME("Доход"),
        EXPENSE("Расход");
//        TRANSFER("Трансфер");

        private final String alias;

        OperationType(String alias) {
            this.alias = alias;
        }
    }

    @Getter
    public enum Type {
        BASIC("Простой", 1), // не автоматизированный, создаешь когда захочешь
        RECURRING ("Регулярный", 2), // полуавтоматизированный, следующая операция создается на основе шаблона
        FIXED("Фиксированный", 3); // автоматизированный, оперции созаются все и сразу

        private final String alias;
        private final int order;

        Type(String alias, int order) {
            this.alias = alias;
            this.order = order;
        }

        public static List<Type> getTypeList() {
            List<Type> excludeTypes = List.of(Type.BASIC);
            return Arrays.stream(Type.values())
                    .filter(t -> !excludeTypes.contains(t))
                    .collect(Collectors.toList());
        }
    }
}
