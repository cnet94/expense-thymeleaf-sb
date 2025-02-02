package org.aturkov.expense.dao.historytransaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoryTransactionRepository extends CrudRepository<HistoryTransactionEntity, UUID> {
}
