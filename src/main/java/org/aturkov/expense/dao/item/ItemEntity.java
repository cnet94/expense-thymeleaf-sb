package org.aturkov.expense.dao.item;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.OperationType;

import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Getter
    public enum Status {
        active("Активный"),
        inactive("Неактивный"),
        archive("Архивный");
        
        private final String value;
        
        Status(String value) {
            this.value = value;
        }
    }
}
