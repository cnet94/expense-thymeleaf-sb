package org.aturkov.expense.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
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
@Table(name = "expense")
public class ExpenseEntity {
    @Id
    private UUID id;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @Column(name = "name")
    private String name;

    @Column(name = "expense_type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "payment_day")
    private int paymentDay;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Column(name = "payment_period_type")
    @Enumerated(EnumType.STRING)
    private ValidityPeriod.Time period;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createAt;

    @OneToMany
    @JoinColumn(name = "expense_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private List<ExpenseDetailEntity> details;

    @Transient
    private Balance generalBalance;

    @Getter
    public enum Type {
        UNKNOWN("", 0),
        DEBT_CREDIT("Долг -> Кредит", 1),
        DEBT_SIMPLE("Долг -> Простой", 2),
        PAYMENT_RECURRING("Платеж -> Регулярный", 2),
        PAYMENT_ONE_TIME("Платеж -> Разовый", 2),
        BUDGET("Бюджет", 3);

        private final String alias;
        private final int order;

        Type(String alias, int order) {
            this.alias = alias;
            this.order = order;
        }

        public static String name(String type) {
            return Arrays.stream(ExpenseEntity.Type.values()).filter(t -> t.alias.equals(type)).findAny().orElse(UNKNOWN).getAlias();
        }
    }
}
