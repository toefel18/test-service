package com.intergamma.inventory.access;

import com.intergamma.inventory.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    /**
     * @param productCode the functional key
     */
    Optional<Product> findByProductCode(String productCode);
}
