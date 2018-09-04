package com.intergamma.inventory.access;

import com.intergamma.inventory.entity.InventoryHistory;
import com.intergamma.inventory.entity.InventoryHistoryType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface InventoryHistoryRepository extends CrudRepository<InventoryHistory, Long> {

    @Query("SELECT SUM(it.amount) FROM InventoryHistory it " +
            " WHERE it.store.name = ?1" +
            " AND it.product.productCode = ?2" +
            " AND it.transactionType = ?3")
    Optional<Long> calculateTotalAmountByType(String storeName, String productCode, InventoryHistoryType transactionType);
}
