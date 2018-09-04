package com.intergamma.inventory.controller;

import com.intergamma.inventory.access.StoreRepository;
import com.intergamma.inventory.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("/stores")
    public Iterable<Store> findAll() {
            return storeRepository.findAll();
    }

    @GetMapping("/stores/{name}")
    public Optional<Store> findById(@PathVariable String name) {
        return storeRepository.findByName(name);
    }

    @PostMapping("/stores")
    public Store createNewStore(@RequestBody Store store) {
        store.setId(null);
        return storeRepository.save(store);
    }

    @DeleteMapping("/stores/{name}")
    public ResponseEntity<Store> delete(@PathVariable String name) {
        Optional<Store> store = storeRepository.findByName(name);
        if (store.isPresent()) {
            storeRepository.delete(store.get());
            //TODO delete all transactions too!
            return ResponseEntity.ok(store.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
