package org.aturkov.expense.dao.itemgroup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemGroupRepository extends CrudRepository<ItemGroupEntity, UUID> {
}
