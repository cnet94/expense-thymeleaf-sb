package org.aturkov.expense.dao.item;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

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

    public enum Status {
        active("Активный"),
        inactive("Неактивный"),
        archive("Архивный");
        
        private final String value;
        
        Status(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
