package org.aturkov.expense.dao.itemgroup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemGroupMapRepository extends CrudRepository<ItemGroupMapEntity, UUID> {
    @Override
    List<ItemGroupMapEntity> findAll();
}
