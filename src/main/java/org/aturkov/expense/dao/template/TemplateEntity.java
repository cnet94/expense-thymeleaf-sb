package org.aturkov.expense.dao.template;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.Balance;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.ValidityPeriod;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Arrays;
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
    private boolean weekend;

    @Column(name = "depend_template_id")
    private UUID dependTemplateId;

    @Column(name = "deposit_id")
    private UUID depositId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createAt;

    @Column(name = "active")
    private Boolean active;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "depend_template_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private TemplateEntity dependTemplateEntity;

    @OneToMany
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private List<ExpenseDetailEntity> details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private DepositEntity deposit;

    @Transient
    private TemplateEntity dependTemplate;

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
        EXPENSE("Расход"),
        TRANSFER("Трансфер");

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

        public static String getCurrentAlias(String type) {
            return Arrays.stream(Type.values()).filter(t -> t.alias.equals(type)).findAny().orElse(BASIC).getAlias();
        }

        public static Type getCurrentType(Type type) {
            return Arrays.stream(Type.values()).filter(t -> t.equals(type)).findAny().orElse(BASIC);
        }
    }
}
