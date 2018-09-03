package com.intergamma.inventory.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Should be an immutable transaction log of inventory changes.
 */
@Entity
@Table(name = "inventory_history")
public class InventoryHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false, updatable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private InventoryHistoryType transactionType;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime transactionTimestamp;

    @Column(nullable = false, updatable = false)
    private long amount;

    public InventoryHistory(Store store, Product product, String clientName, InventoryHistoryType transactionType, long amount) {
        this.store = store;
        this.product = product;
        this.clientName = clientName;
        this.transactionType = transactionType;
        this.transactionTimestamp = ZonedDateTime.now();
        this.amount = amount;
        if (this.amount <= 0) {
            throw new IllegalArgumentException("amount cannot be 0 or lower, but was " + amount);
        }
    }

    public InventoryHistory() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ZonedDateTime getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(ZonedDateTime transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public InventoryHistoryType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(InventoryHistoryType transactionType) {
        this.transactionType = transactionType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
