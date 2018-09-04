package com.intergamma.inventory.access;

import com.intergamma.inventory.entity.Store;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface StoreRepository extends CrudRepository<Store, Long> {
    /**
     * @param name the functional key
     */
    Optional<Store> findByName(String name);
}
