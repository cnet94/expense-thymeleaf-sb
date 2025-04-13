package org.aturkov.expense.service.item;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dao.item.ItemRepository;
import org.aturkov.expense.exception.ErrorCodeExpense;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.service.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;

    public List<ItemEntity> findItems() {
        return itemRepository.findAll();
    }

    public ItemEntity createItem(ItemEntity entity) {
        entity
                .setName("[" + entity.getOperationType().getAlias() + "] " + entity.getName())
                .setStatus(ItemEntity.Status.active);
        return itemRepository.save(entity);
    }

    public void deleteItem(UUID id) throws Exception {
        if (Permissions.SYSTEM_ITEM.getId().equals(id))
            throw new ServiceException("Can't delete system item");
        itemRepository.deleteById(id);
    }

    public void archiveItem(UUID id) throws ServiceException {
        throw new ServiceException("Not implemented");
    }
}
