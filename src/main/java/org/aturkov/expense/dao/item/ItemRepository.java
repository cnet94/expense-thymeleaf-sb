package org.aturkov.expense.dao.item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, UUID> {
    @Override
    List<ItemEntity> findAll() throws RuntimeException;
}
