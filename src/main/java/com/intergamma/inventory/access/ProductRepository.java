package com.intergamma.inventory.access;

import com.intergamma.inventory.entity.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface ProductRepository extends CrudRepository<Product, Long> {
    /**
     * @param productCode the functional key
     */
    Optional<Product> findByProductCode(String productCode);
}
