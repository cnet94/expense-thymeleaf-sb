package org.aturkov.expense.dao.item;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.itemgroup.ItemGroupEntity;

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

    @Column(name = "item_group_id")
    private UUID itemGroupId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "item_group_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ItemGroupEntity itemGroup;

    public enum Status {
        active,
        inactive,
        archive
    }
}
