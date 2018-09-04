package com.intergamma.inventory.controller;

import com.intergamma.inventory.access.ProductRepository;
import com.intergamma.inventory.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public Iterable<Product> findAll() {
            return productRepository.findAll();
    }

    @GetMapping("/products/{productCode}")
    public Optional<Product> findById(@PathVariable String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    @PostMapping("/products")
    public Product createNewProduct(@RequestBody Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    @DeleteMapping("/products/{productCode}")
    public ResponseEntity<Product> delete(@PathVariable String productCode) {
        Optional<Product> product = productRepository.findByProductCode(productCode);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
