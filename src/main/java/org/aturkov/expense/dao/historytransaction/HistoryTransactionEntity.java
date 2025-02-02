package org.aturkov.expense.dao.historytransaction;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "history_transaction")
public class HistoryTransactionEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "operation_status")
    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private TemplateEntity.OperationType operationType;

    @Column(name = "expense_detail_id")
    private UUID expenseDetailId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "src_deposit_id")
    private UUID srcDepositId;

    @Column(name = "dst_deposit_id")
    private UUID dstDepositId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createAt;

    public enum OperationStatus{
        APPROVE, CANCEL
    }
}
