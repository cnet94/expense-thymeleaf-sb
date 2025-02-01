package org.aturkov.expense.dao.deposit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepositRepository extends CrudRepository<DepositEntity, UUID> {
    @Override
    List<DepositEntity> findAll();
}
