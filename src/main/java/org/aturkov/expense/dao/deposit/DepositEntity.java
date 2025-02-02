package org.aturkov.expense.dao.deposit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;

import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "deposit")
public class DepositEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public void subtractAmount(double amount) {
        this.amount -= amount;
    }

    public enum Status {
        active,
        archived,
        trashed
    }
}
