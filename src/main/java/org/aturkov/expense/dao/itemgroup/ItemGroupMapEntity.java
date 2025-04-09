package org.aturkov.expense.dao.itemgroup;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.aturkov.expense.dao.item.ItemEntity;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "item_group_map")
@IdClass(ItemGroupMapEntity.PK.class)
public class ItemGroupMapEntity {
    @Id
    @Column(name = "item_group_id")
    private UUID itemGroupId;

    @Id
    @Column(name = "item_id")
    private UUID itemId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "item_group_id", insertable = false, updatable = false)
    private ItemGroupEntity itemGroup;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;

    @Data
    @EqualsAndHashCode
    public static class PK implements Serializable {
        private UUID itemGroupId;
        private UUID itemId;
    }
}
